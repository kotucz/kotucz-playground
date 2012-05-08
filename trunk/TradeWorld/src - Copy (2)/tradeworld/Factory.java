package tradeworld;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Kotuc
 */
public class Factory extends Building {

    private final List<ProductionChain> productionChains = new LinkedList<ProductionChain>();

    public Factory(Type type, Player owner, Pos pos) {

        super(type, owner, pos);

        productionChains.add(new ProductionChain());  //TODO: This is only example for testing. It should be in constructor parameters.
    }

    public Collection<ProductionChain> getProductionChains() {

        return productionChains;
    }

}
