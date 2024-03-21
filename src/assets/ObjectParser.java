package assets;

public interface ObjectParser<T> {
  T[] parseObjects(String[] strings);

  T parseObject(String string);
}
