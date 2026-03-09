package lab4;
/**
 * Concrete decorator class that adds Peppers as a topping to a Pizza.
 *
 * This class wraps an existing Pizza object and:
 * - Appends "Peppers" to the pizza description
 * - Adds 1.79 to the total price
 * * @author Daniel Olusegun
 * @since 02/03/2026
 * 24362731
 * It represents a Concrete Decorator in the Decorator Design Pattern.
 */

public class Peppers extends PizzaDecorator {

    private final Pizza pizza;

    public Peppers(Pizza pizza) {
        this.pizza = pizza;
    }

    @Override
    public String getDesc() {
        return pizza.getDesc() + ", Peppers (1.79)";
    }

    @Override
    public double getPrice() {
        return pizza.getPrice() + 1.79;
    }
}