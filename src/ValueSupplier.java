import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

//делаем аннотацию доступной для рефлексии
@Retention(RetentionPolicy.RUNTIME)
@interface AutoFillValue {
}

class ValueSupplier {
    //hashMap фабрик
    private final Map<Class<?>, Supplier<?>> factories = new HashMap<>();

    //метод добавления фабрики
    public <T> void addValueFactory(Class<? extends T> clazz, Supplier<T> valueFactory) {
        factories.put(clazz, valueFactory);
    }

    //метод заполение полей метода по правилам, записанным в hashMap
    public void fillValues(Object object) throws IllegalAccessException {
        for (Field field : object.getClass().getDeclaredFields()) {
            boolean isAnnotatedCorrectly = !(field.getAnnotation(AutoFillValue.class) == null);
            boolean isTypeCorrect = factories.containsKey(field.getType());
            if (isAnnotatedCorrectly && isTypeCorrect) {
                for (Type key : factories.keySet()) {
                    if (key == field.getType()) {
                        //открываем доступ к private полям
                        field.setAccessible(true);
                        //записываем значения для объекта, переданного в аргументы
                        field.set(object, factories.get(key).get());
                    }
                }
            }
        }
    }
}