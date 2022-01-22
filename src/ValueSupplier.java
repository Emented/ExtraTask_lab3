import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

//делаем аннотацию доступной для рефлексии

//в какой момент доступна аннотация (в данном случае в ходе выполнения программы)
@Retention(RetentionPolicy.RUNTIME)
//указывает, какой элемент программы будет использоваться аннотацией
@Target(ElementType.FIELD)
@interface AutoFillValue {
}

class ValueSupplier {
    //hashMap фабрик
    private final Map<Class<?>, Supplier<?>> factories = new HashMap<>();

    //метод который позволяет добавить фабрику для определенного типа поля
    public <T> void addValueFactory(Class<? extends T> clazz, Supplier<T> valueFactory) {
        if (factories.containsKey(clazz)) throw new IllegalArgumentException("фабрика для данного типа уже сущетвует");
        factories.put(clazz, valueFactory);
    }

    //метод заполение полей метода по правилам, записанным в hashMap
    public void fillValues(Object object) {
        for (Field field : object.getClass().getDeclaredFields()) {
            boolean isAnnotatedCorrectly = !(field.getAnnotation(AutoFillValue.class) == null);
            boolean isTypeCorrect = factories.containsKey(field.getType());
            if (isAnnotatedCorrectly && isTypeCorrect) {
                try {
                    //открываем доступ к private полям
                    field.setAccessible(true);
                    //записываем значения для объекта, переданного в аргументы
                    field.set(object, factories.get(field.getType()).get());
                } catch (IllegalAccessException e) {
                    System.out.println("ошибка доступа к полю " + field.getName());
                } catch (IllegalArgumentException e) {
                    System.out.println("объект: " + object.getClass().getName() + " не является экземпляром класса, объявляющего поле: " + field.getName() + ", либо произошла ошибка при преобразовании типов");
                }
            }
        }
    }
}