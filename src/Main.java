import java.util.SortedMap;

public class Main {
    public static void main(String[] args) {
        ValueSupplier valueSupplier = new ValueSupplier();
        valueSupplier.addValueFactory(String.class, () -> "some string");
        valueSupplier.addValueFactory(int.class, () -> 42);
//        valueSupplier.addValueFactory(double.class, () -> 1.25);


        TestClass t = new TestClass();
        System.out.println("Before:");
        t.printer();
        valueSupplier.fillValues(t);
        System.out.println("After:");
        t.printer();
    }
}
