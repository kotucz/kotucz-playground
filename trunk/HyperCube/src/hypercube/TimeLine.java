package hypercube;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Kotuc
 */
public class TimeLine {

    private Map<Integer,List<TimeAction>> timeline = new HashMap<Integer,List<TimeAction>>();

    /**
     *
     * @param time
     * @param action
     */
    public void addAction(int time, TimeAction action) {
//        List<Action> acts = actions.get(time);
//        if (acts==null) {
//            acts = new ArrayList<Entity.Action>();
//            actions.put(time, acts);
//        }
//        acts.add(action);
//        System.out.println(""+actions);
        getActionsRef(time).add(action);
    }

    public Collection<TimeAction> getActions(int time) {
        return Collections.unmodifiableCollection(getActionsRef(time));
    }


    private Collection<TimeAction> getActionsRef(int time) {
        if (!timeline.containsKey(time)) {
            timeline.put(time, new ArrayList<TimeAction>());
        }
        return timeline.get(time);
    }


}
