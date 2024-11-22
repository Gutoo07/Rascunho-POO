import java.util.HashMap;
import java.util.Map;

public class PersistenceDataPage {
    
    private static Map<String,Object> map;

    public PersistenceDataPage(){
        map = new HashMap();
    }

    public void add(String nameData, Object value){
        map.put(nameData, value);
    }

    public void delete(String nameData){
        map.remove(nameData);
    }

    public Object get(String nameData){
        return get(nameData);
    }

}
