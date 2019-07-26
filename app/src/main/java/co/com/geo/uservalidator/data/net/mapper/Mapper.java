package co.com.geo.uservalidator.data.net.mapper;

import java.util.List;

public interface Mapper<R, T> {
    public T transform(R input);
    public List<T> transformList(List<R> inputList);
}
