// Artem Tagintsev, Dots
// This class represents the individual boxes in the grid, keeps track of which sides have been drawn, and which player has completed a box
public class Boxes {
	public boolean top; // Indicate if top of box has been drawn
	public boolean bottom; // Indicate if bottom of box has been drawn
	public boolean left; // Indicate if left of box has been drawn
	public boolean right; // Indicate if right of box has been drawn
	public String whosBox; // Stores initial of the player who completed a box
	
	// Constructor that initializes all sides as now drawn, false, and the box as unclaimed by a player
	public Boxes() {
		top = false;
		bottom = false;
		left = false;
		right = false;
		whosBox = " ";
	}
	
	// Method to check if all the sides of box have been drawn
	public boolean boxComplete() {
		if(top && bottom && left && right) return true;
		else return false;
	}
	
	// Method to draw the top side of the box if it hasn't yet been drawn
	public boolean drawTop() {
		if(!top) { // If top hasn't been drawn yet
			top = true; // Set top to true
			return true; // Return top as true
		}
		// else if top has been drawn already, return false
		return false; 
	}
	
	// Method to draw the bottom side of the box if it hasn't yet been drawn
	public boolean drawBottom() {
		if(!bottom) {
			bottom = true;
			return true;
		}
		return false;
	}
	
	// Method to draw the left side of the box if it hasn't yet been drawn
	public boolean drawLeft() {
		if(!left) {
			left = true;
			return true;
		}
		return false;
	}
	
	// Method to draw the right side of the box if it hasn't yet been drawn
	public boolean drawRight() {
		if(!right) {
			right = true;
			return true;
		}
		return false;
	}
}
