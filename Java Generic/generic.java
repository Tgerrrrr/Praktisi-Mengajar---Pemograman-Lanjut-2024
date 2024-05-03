package generic;

import java.util.*;

public class generic<T> {
    private T data;

    public generic(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
