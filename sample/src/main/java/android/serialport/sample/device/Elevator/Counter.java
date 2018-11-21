package android.serialport.sample.device.Elevator;

/**
 * Created by lc on 2018/11/4.
 */

public class Counter {

    private int start;
    private int mid;
    private int end;

    public Counter(int start,int mid,int end){
        this.start = start;
        this.mid = mid;
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}
