package ec.edu.espe.corebancario.transactions.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ApplicationValues {

    private final String mongoHost;
    private final String mongoDb;
    private final String username;
    private final String pwd;

    @Autowired
    public ApplicationValues(
            @Value("${transactions.mongo.host}") String mongoHost, 
            @Value("${transactions.mongo.db}") String mongoDb,
            @Value("${transactions.mongo.username}") String username, 
            @Value("${transactions.mongo.pwd}") String pwd) {
        this.mongoHost = mongoHost;
        this.mongoDb = mongoDb;
        this.username = username;
        this.pwd = pwd;
    }

}
