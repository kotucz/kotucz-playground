package tradeworld;

import tradeworld.chains.ProductionChain;
import tradeworld.chains.ProductionChainDefinitions;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import javax.vecmath.Point3d;
import tradeworld.graphics.Notification3D;
import tradeworld.gui.FactoryPanel;

/**
 *
 * @author Kotuc
 */
public class Factory extends Building {

    private final List<ProductionChain> productionChains = new LinkedList<ProductionChain>();
    private final List<Storage> storages = new LinkedList<Storage>();
    private ProductionChain selectedProductionChain;
    private int progress;
    private WorkingState workingState;
    private EnableState enableState;

    public Factory(Type type, Player owner, Point3d center) {

        super(type, owner, center);

        productionChains.addAll(ProductionChainDefinitions.INSTANCE.getProductionChains(type));
        selectProductionChain(productionChains.get(0));

        progress = 0;
        workingState = WorkingState.WAITING_FOR_INPUTS;
        enableState = EnableState.GO;

        this.panel = new FactoryPanel(this);

    }

    /**
     * Selects production chain (if in productionChains list).
     * @param productionChain Chain to select
     */
    public void selectProductionChain(ProductionChain productionChain) {

        if (productionChains.contains(productionChain)) {
            selectedProductionChain = productionChain;
        } else {
            throw new IllegalArgumentException("Cannot select chain which is not in productionChains list of the factory!");

        }
    }

    /**
     * Adds storage (in the STORE_RADIUS) to the factory storage list.
     * @param storage
     */
    public void addStorage(Storage storage) {

        //System.out.println("" + getPos().distanceTo(storage.getPos()));
        if (getPos().distance(storage.getPos()) <= Building.STORAGE_RADIUS) {
            storages.add(storage);
            Collections.sort(storages, new Comparator<Storage>() {

                public int compare(Storage o1, Storage o2) {
                    double dst1 = getPos().distance(o1.getPos());
                    double dst2 = getPos().distance(o2.getPos());
                    if (o1.equals(o2)) {
                        return 0;
                    } else {
                        if (dst1 == dst2) {
                            return o1.getName().compareTo(o2.getName());
                        } else {
                            if (dst1 < dst2) {
                                return -1;
                            } else {
                                return 1;
                            }
                        }
                    }
                }
            });

        } else {

            throw new IllegalArgumentException("Storage is out of STORAGE_RADIUS.");
        }
    }

    @Override
    public void act(Time time) {
        doStep();
    }

    /**
     * Realizes one step in the factory's life.
     */
    public void doStep() {
//        System.out.println("Factor " + workingState);
        if (EnableState.GO.equals(enableState)) {


            if (WorkingState.WORKING.equals(workingState)) {


                if (selectedProductionChain != null) {
                    if (progress >= selectedProductionChain.getSpeed()) {
//                        getWorld().showNotification(Notification3D.createTextNotification("WOOD", new Color3f(0, 1, 0)), this.getPos());
                        Notification3D goodsIcon = Notification3D.createGoodsIcon(new Goods(Goods.Type.WOOD, 1, owner));
                        getWorld().showNotification(goodsIcon, this.getPos());
                        setWorkingState(WorkingState.WAITING_FOR_STORAGE);
                    } else {
                        progress++;
                    }

                } else {
                    throw new IllegalStateException("Factory is producing and has not selected production chain!");
                }

            }

            if (WorkingState.WAITING_FOR_INPUTS.equals(workingState)) {

                if (selectedProductionChain != null) {

                    boolean foundEnoughGoods = true;
                    //Checks if there is enogh input goods
                    for (Product inputGoods : selectedProductionChain.getInputs()) {
                        if (!isGoodsAvailable(new Goods(inputGoods.getGoodsType(), inputGoods.getRatio(), this.getOwner()))) {
                            foundEnoughGoods = false;

                        }
                    }

                    if (foundEnoughGoods) {


                        //Removes input goods
                        for (Product inputGoods : selectedProductionChain.getInputs()) {
                            int countLeft = inputGoods.getRatio();
                            while (countLeft > 0) {
                                for (Storage storage : storages) {
                                    Goods targetGoods = storage.getPayload().findGoodsPile(inputGoods.getGoodsType(), this.getOwner());
                                    if (targetGoods != null) {
                                        int toRemove = Math.min(targetGoods.getQuantity(), countLeft);
                                        storage.getPayload().removeGoods(new Goods(inputGoods.getGoodsType(), toRemove, this.getOwner()));
                                        countLeft -=
                                                toRemove;
                                        break;
                                    }
                                }
                            }
                        }
                        setWorkingState(WorkingState.WORKING);
                        setProgress(0);
                    } else {
                        //TODO: What if there isnt enough input goods?
                    }
                } else {

                    throw new IllegalStateException("Factory wants to start working, but have not selected production chain");
                }
            }

            if (WorkingState.WAITING_FOR_STORAGE.equals(workingState)) {


                int spaceWanted = 0;
                //Checks if there is enogh space to store produced goods
                for (Product outputGoods : selectedProductionChain.getOutputs()) {
                    spaceWanted += outputGoods.getRatio();
                }

                for (Storage storage : storages) {
                    spaceWanted -= storage.getPayload().getFreeVolume();
                }

                if (spaceWanted <= 0) { //found enough space
                    //Stores output goods
                    for (Product product : selectedProductionChain.getOutputs()) {
                        for (int i = 0; i < product.getRatio(); i++) {
                            Storage storage = findFreeStorage(1);
                            if (storage != null) {
                                storage.getPayload().addGoods(new Goods(product.getGoodsType(), 1, this.getOwner()));
                            } else {
                                throw new IllegalStateException("Free space in a storage has vanished during storing outputs!");
                            }
                        }
                    }
                    setWorkingState(WorkingState.WAITING_FOR_INPUTS);
                }
            }
        }
    }

    /**
     * Finds first storage with capacity at <code> least spaceRequired </code>, null if not found.
     * @param spaceRequired Minimal required space in the storage
     * @return Storege with required free space or null, if not found
     */
    private Storage findFreeStorage(int spaceRequired) {

        for (Storage storage : storages) {
            if (storage.getPayload().getFreeVolume() >= spaceRequired) {
                return storage;
            }

        }
        return null;
    }

    /**
     * Checks if there is enough <code> goods </code> in the storages associated to the factory.
     * @param goods Required goods
     * @return True, if there is enough <code> goods </code>
     */
    private boolean isGoodsAvailable(Goods goods) {

        int count = goods.getQuantity();
        for (Storage storage : storages) {
            if (storage.getPayload().findGoodsPile(goods.getType(), goods.getOwner()) != null) {
                count -= storage.getPayload().findGoodsPile(goods.getType(), goods.getOwner()).getQuantity();
                if (count <= 0) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Stops factory's production (<code> enableState </code>).
     */
    public void stopWorking() {

        if (getEnableState() == EnableState.GO) {
            setEnableState(EnableState.STOP);
        } else {
            throw new IllegalArgumentException("Factory is already stopped.");
        }

    }

    /**
     * Starts factory's production (<code> enableState </code>).
     */
    public void startWorking() {

        if (getEnableState() == EnableState.STOP) {
            setEnableState(EnableState.GO);
        } else {
            throw new IllegalArgumentException("Factory is already working.");
        }

    }

    public List<ProductionChain> getProductionChains() {

        return productionChains;
    }

    public WorkingState getWorkingState() {

        return workingState;
    }

    private void setWorkingState(WorkingState state) {

        this.workingState = state;
    }

    public EnableState getEnableState() {

        return enableState;
    }

    private void setEnableState(EnableState state) {

        this.enableState = state;
    }

    public int getProgress() {

        return progress;
    }

    private void setProgress(int progress) {

        this.progress = progress;
    }

    public ProductionChain getSelectedProductionChain() {

        return selectedProductionChain;
    }

    public enum WorkingState {

        WORKING,
        WAITING_FOR_INPUTS,
        WAITING_FOR_STORAGE;
    }

    public enum EnableState {

        STOP,
        GO;
    }
}
