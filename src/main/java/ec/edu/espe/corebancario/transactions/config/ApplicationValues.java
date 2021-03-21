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

    @Autowired
    public ApplicationValues(
            @Value("${transactions.mongo.host}") String mongoHost, 
            @Value("${transactions.mongo.db}") String mongoDb) {
        this.mongoHost = mongoHost;
        this.mongoDb = mongoDb;
    }

}
