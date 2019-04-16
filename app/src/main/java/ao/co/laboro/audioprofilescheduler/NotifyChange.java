package ao.co.laboro.audioprofilescheduler;

public class NotifyChange {
    private boolean changed;
    private static NotifyChange mInstance;

    private NotifyChange(){

    }

    public static NotifyChange getIntance(){
        if(mInstance == null){
            return mInstance = new NotifyChange();
        }else{
            return mInstance;
        }
    }

    public boolean isChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }
}
