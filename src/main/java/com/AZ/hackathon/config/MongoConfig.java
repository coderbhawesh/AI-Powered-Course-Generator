package com.AZ.hackathon.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.util.StringUtils;

import java.util.List;

@Configuration
public class MongoConfig {

    private static final Logger log = LoggerFactory.getLogger(MongoConfig.class);
    private static final List<String> MONGODB_URI_KEYS = List.of(
            "spring.data.mongodb.uri",
            "SPRING_DATA_MONGODB_URI",
            "MONGODB_URI",
            "MONGO_URL",
            "MONGO_URI"
    );
    private static final List<String> MONGODB_DATABASE_KEYS = List.of(
            "spring.data.mongodb.database",
            "SPRING_DATA_MONGODB_DATABASE",
            "MONGODB_DATABASE"
    );

    @Bean
    public MongoClient mongoClient(Environment environment) {
        ConnectionString connectionString = new ConnectionString(resolveMongoUri(environment));
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .serverApi(ServerApi.builder().version(ServerApiVersion.V1).build())
                .build();

        return MongoClients.create(settings);
    }

    @Bean
    public MongoDatabaseFactory mongoDatabaseFactory(MongoClient mongoClient, Environment environment) {
        ConnectionString connectionString = new ConnectionString(resolveMongoUri(environment));
        String databaseName = resolveDatabaseName(connectionString, environment);
        return new SimpleMongoClientDatabaseFactory(mongoClient, databaseName);
    }

    @Bean
    public ApplicationRunner mongoStartupCheck(
            MongoDatabaseFactory mongoDatabaseFactory,
            Environment environment,
            @Value("${app.mongodb.ping-on-startup:true}") boolean pingOnStartup
    ) {
        return args -> {
            log.info("MongoDB connection configured using property source '{}'", resolveMongoUriSource(environment));

            if (!pingOnStartup) {
                log.info("MongoDB startup ping is disabled");
                return;
            }

            mongoDatabaseFactory.getMongoDatabase().runCommand(new Document("ping", 1));
            log.info("MongoDB ping succeeded for database '{}'", mongoDatabaseFactory.getMongoDatabase().getName());
        };
    }

    private String resolveMongoUri(Environment environment) {
        String mongoUri = firstNonBlank(environment, MONGODB_URI_KEYS);
        if (!StringUtils.hasText(mongoUri)) {
            throw new IllegalStateException(
                    "MongoDB URI is missing. Set SPRING_DATA_MONGODB_URI or MONGODB_URI for deployment."
            );
        }
        return mongoUri;
    }

    private String resolveMongoUriSource(Environment environment) {
        for (String key : MONGODB_URI_KEYS) {
            if (StringUtils.hasText(environment.getProperty(key))) {
                return key;
            }
        }
        return "unknown";
    }

    private String resolveDatabaseName(ConnectionString connectionString, Environment environment) {
        if (StringUtils.hasText(connectionString.getDatabase())) {
            return connectionString.getDatabase();
        }

        String databaseName = firstNonBlank(environment, MONGODB_DATABASE_KEYS);
        if (!StringUtils.hasText(databaseName)) {
            throw new IllegalStateException(
                    "MongoDB database name is missing. Add the database to the URI or set SPRING_DATA_MONGODB_DATABASE/MONGODB_DATABASE."
            );
        }
        return databaseName;
    }

    private String firstNonBlank(Environment environment, List<String> keys) {
        for (String key : keys) {
            String value = environment.getProperty(key);
            if (StringUtils.hasText(value)) {
                return value.trim();
            }
        }
        return null;
    }
}
