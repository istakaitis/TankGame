package gui;

/**
 * Class generates a fun fact from the database or a preset list of funky saying for display on the login screen.
 * @author Oliver James Richards
 * @version 13th March 2020
 */

public class FunFact {

    /**
     * The class only has one field variable, the String that is displayed on the login screen.
     */

    private String fact;

    /**
     * Constructor assigns one of ten facts to the field variable face. Can be extended by altering probabilities.
     * Feel free to add anything which may amuse you.
     * @return is the phrase.
     */

    public FunFact() {

        double random = Math.random();
        if (random > 0.9) {
        	 fact = "Tanks are red, Tanks are blue, when I shoot my missile, I destroy you!";
        } else if (random > 0.8) {
            fact = "Most dreams last only 5 to 20 minutes. Red dreams about Blue for hours!";
        } else if (random > 0.7) {
            fact = "An olive tree can live up to 1,500 years. Tanks live until shot!";
        } else if (random > 0.6) {
            fact = "Cold water weighs more than hot water. That means Blue weighs more than Red!";
        } else if (random > 0.5) {
            fact = "By raising your legs slowly and laying on your back, you can't sink in quicksand.";
        } else if (random > 0.4) {
            fact = "Recycling one glass jar saves enough energy to watch television for 3 hours.";
        } else if (random > 0.3) {
            fact = "Don't look so blue!";
        } else if (random > 0.2) {
            fact = "Now with 200% more blue!";
        } else if (random > 0.1) {
            fact = "Never trust red!";
        } else {
        	fact = "A small child could swim through the veins of a blue whale.";
        }
    }

    public String toString() {

        return this.fact;

    }

}
