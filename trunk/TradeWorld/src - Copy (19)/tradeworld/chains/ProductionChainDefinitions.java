/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tradeworld.chains;

import tradeworld.*;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import tradeworld.Building.Type;

/**
 *
 * @author Petr Dluhoš
 */
public enum ProductionChainDefinitions {

    INSTANCE;
    private Map<Type, List<ProductionChain>> productionChainsDefinitions = new EnumMap<Building.Type, List<ProductionChain>>(Type.class);

    {

        List<ProductionChain> chains;
        ProductionChain chain;

        chains = new LinkedList<ProductionChain>();
        chain = new ProductionChain();
        chain.addOutput(new Product(Goods.Type.WOOD, 1));
        chain.setSpeed(1000);
        chains.add(chain);
        productionChainsDefinitions.put(Type.LUMBERJACK_CAMP, chains);

        chains = new LinkedList<ProductionChain>();
        chain = new ProductionChain();
        chain.addInput(new Product(Goods.Type.WOOD, 1));
        chain.addOutput(new Product(Goods.Type.BOARD, 2));
        chain.setSpeed(1400);
        chains.add(chain);
        productionChainsDefinitions.put(Type.WOODMILL, chains);

        chains = new LinkedList<ProductionChain>();
        chain = new ProductionChain();
        chain.addInput(new Product(Goods.Type.BOARD, 2));
        chain.addOutput(new Product(Goods.Type.FURNITURE, 1));
        chain.setSpeed(1800);
        chains.add(chain);
        chain = new ProductionChain();
        chain.addInput(new Product(Goods.Type.BOARD, 2));
        chain.addOutput(new Product(Goods.Type.WOOD, 2));
        chain.setSpeed(1400);
        chains.add(chain);
        productionChainsDefinitions.put(Type.FURNITURE_FACTORY, chains);



    }

    public List<ProductionChain> getProductionChains(Type type) {

        return productionChainsDefinitions.get(type);
    }
}
