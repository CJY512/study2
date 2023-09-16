package practice03_interface;

public class Car implements Repairable{
    @Override
    public void repair() {

    }

    @Override
    public void d_method() {
        Repairable.super.d_method();
    }
}
