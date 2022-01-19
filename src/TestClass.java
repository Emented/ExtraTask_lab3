class TestClass {
    // не будет заполнено - нет аннотации
    private String a;
    // будет заполнено
    @AutoFillValue
    private String b;
    // будет заполнено
    @AutoFillValue
    private int c;
    // не будет заполнено - нет фабрики значений для типа double
    @AutoFillValue
    private double d;

    public void printer() {
        System.out.println("a = " + a + "\nb = " + b + "\nc = " + c + "\nd = " + d);
    }
}
