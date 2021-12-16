package com.franciscodadone.model.remote.queries;

import com.franciscodadone.model.local.queries.ProductsQueries;
import com.franciscodadone.model.remote.MongoConnection;
import com.franciscodadone.model.models.Product;
import com.franciscodadone.util.Logger;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;

import static com.mongodb.client.model.Updates.set;

public class RemoteStockQueries {

    private static long getMongoProductsCount() {
        MongoConnection mongoConnection = new MongoConnection();
        long count = mongoConnection.mongoStock.countDocuments(new Document());
        mongoConnection.close();
        return count;
    }

    protected static boolean isDatabaseOutdated() {
        MongoConnection mongoConnection = new MongoConnection();
        Logger.log("Checking (Stock) Collection on Mongo...");

        long localRegisteredStock  = ProductsQueries.getAllProducts().size();
        long remoteRegisteredStock = getMongoProductsCount();

        if(localRegisteredStock > remoteRegisteredStock) { // makes the backup on remote
            ProductsQueries.getAllProducts().forEach((product) -> {
                Document mongoQuery = (Document) mongoConnection.mongoStock.find(Filters.eq("code", product.getCode())).first();
                if(mongoQuery == null) {
                    backupProduct(product);
                }
            });
        } else if(localRegisteredStock == 0 && localRegisteredStock != remoteRegisteredStock) { // if the local database is empty, retrieves from remote
            return true;
        }

        ArrayList<Product> remoteProducts = getAllProducts();
        for(Product localProduct : ProductsQueries.getAllProducts()) {
            if(remoteProducts.contains(localProduct)) break;
            for(Product remoteProduct : remoteProducts) {
                if(localProduct.getCode().equals(remoteProduct.getCode()))   {
                    if(
                            !localProduct.getProdName().equals(remoteProduct.getProdName()) ||
                            localProduct.getPrice() != remoteProduct.getPrice() ||
                            localProduct.getQuantity() != remoteProduct.getQuantity() ||
                            !localProduct.getQuantityType().equals(remoteProduct.getQuantityType()) ||
                            localProduct.isDeleted() != remoteProduct.isDeleted() ||
                            localProduct.getMinQuantity() != remoteProduct.getMinQuantity()
                    ) {
                        editProduct(localProduct);
                    }
                    break;
                }
            }
        }
        mongoConnection.close();
        return false;
    }

    protected static void retrieveFromRemote() {
        getAllProducts().forEach((remoteProduct) -> {
            Logger.log("Retrieving product from remote. name=" + remoteProduct.getProdName());
            ProductsQueries.saveProduct(remoteProduct, false);
        });
    }

    public static void backupProduct(Product product) {
        new Thread(() -> {
            MongoConnection mongoConnection = new MongoConnection();
            Logger.log("Making backup of Product '" + product.getProdName() + "'");
            mongoConnection.mongoStock.insertOne(new Document()
                    .append("code", product.getCode())
                    .append("title", product.getProdName())
                    .append("quantity", product.getQuantity())
                    .append("price", product.getPrice())
                    .append("quantityType", product.getQuantityType())
                    .append("deleted", product.isDeleted())
                    .append("minQuantity", product.getMinQuantity())
            );
            mongoConnection.close();
        }).start();
    }

    public static void editProduct(Product product) {
        Logger.log("Editing Product '" + product.getProdName() + "'");
        new Thread(() -> {
            MongoConnection mongoConnection = new MongoConnection();
            Bson filter = Filters.eq("code", product.getCode());

            Bson updateProducts    = set("title", product.getProdName());
            Bson updateDate        = set("quantity", product.getQuantity());
            Bson updatePrice       = set("price", product.getPrice());
            Bson updateSessionID   = set("quantityType", product.getQuantityType());
            Bson updateDeleted     = set("deleted", product.isDeleted());
            Bson updateMinQuantity = set("minQuantity", product.getMinQuantity());

            Bson updates = Updates.combine(updateProducts, updateDate, updateSessionID, updatePrice, updateDeleted, updateMinQuantity);
            mongoConnection.mongoStock.updateOne(filter, updates);

            mongoConnection.close();
        }).start();
    }

    private static ArrayList<Product> getAllProducts() {
        MongoConnection mongoConnection = new MongoConnection();
        FindIterable remoteProducts = mongoConnection.mongoStock.find();

        ArrayList<Product> products = new ArrayList<>();
        remoteProducts.forEach((product) -> {
            products.add(new Product(
                    ((Document)product).getString("code"),
                    ((Document)product).getString("title"),
                    ((Document)product).getDouble("price"),
                    ((Document)product).getInteger("quantity"),
                    ((Document)product).getString("quantityType"),
                    ((Document)product).getBoolean("deleted"),
                    ((Document)product).getInteger("minQuantity")
            ));
        });
        mongoConnection.close();
        return products;
    }

}
