package practice2;

public class CarEx {
    public static void main(String[] args) {
        Car car = new Car(4, "black");
        SportsCar sportsCar = null;
        //noinspection ConstantConditions
//        sportsCar = (SportsCar)car;
//        System.out.println("sportsCar.isOpenAvail() = " + sportsCar.isOpenAvail());

        SportsCar sportsCar1 = new SportsCar(4, "Red", false);
        Car car1 = sportsCar1;
        System.out.println("car1.getColor() = " + car1.getColor());
        sportsCar = (SportsCar) car1;
        System.out.println("sportsCar = " + sportsCar.isOpenAvail());
        if (car1 instanceof SportsCar) {
            System.out.println(" good ");
        }
        if (car instanceof SportsCar) {
            System.out.println(" not good ");
        }
    }
}
