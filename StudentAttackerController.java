package edu.ufl.cise.cs1.controllers;
import game.controllers.AttackerController;
import game.models.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public final class StudentAttackerController implements AttackerController { //Start Class
	public void init(Game game) { }

	public void shutdown(Game game) { }

	public int update(Game game,long timeDue) { //Start Method
		//Called to update the agent, given game state game after time milliseconds have passed. This method returns a
		//direction for the attacker which will be used to move the attack in the next game tick.
		// {UP = 0, RIGHT = 1, DOWN = 2, LEFT =3, EMPTY = -1}
		// Action is basically the direction the attacker is going to go in i.e. action = 1
		// Node is a location, an actor, pill, and power pill can have a location

		//Node nearestBadGuyLoc = null;
		//int distancetoNearestPlayer = 51;
		//Defender defenders = null;
		//int lengthfromnearestBigPill = 50;

		int action = 0;
		Attacker player = game.getAttacker(); // object for the attackers
		Node nearestBigPill = null; //Location of closest power pill
		ArrayList<Node> mascotsOutAbout = new ArrayList<>(); //list of defenders in play area
		List<Node> listofPills = game.getPillList(); //lists pills
		Node nearestLilPill = player.getTargetNode(listofPills, true); //loc of the nearest tiny pills
		List<Node> bigBoyPills = game.getPowerPillList(); //list of the big pills
		Node PlayerLoc = player.getLocation(); //Gives location of the player
		List<Defender> AttackerList = game.getDefenders(); //list of bad guys (defenders)

		for (int i = 0; i < 4; i++) { //compiles a list of all defenders in the playing area
			if (AttackerList.get(i).getLairTime() == 0) {
				mascotsOutAbout.add(AttackerList.get(i).getLocation()); } }

		if (!bigBoyPills.isEmpty()) { //If there is no power pills
			nearestBigPill = player.getTargetNode(bigBoyPills, true); }

		int lengthfromnearestBigPill = 50; // how far the nearest big power pill is from the player
		if (nearestBigPill != null) { // If there is no nearest power pill. Otherwise, it causes an error
			lengthfromnearestBigPill = PlayerLoc.getPathDistance(nearestBigPill); }

		Node nearestBadGuyLoc = null; //The loc of the nearest enemy
		if (!mascotsOutAbout.isEmpty()) { // If there are enemies in the game arena, finds the closest enemy to player
			nearestBadGuyLoc = player.getTargetNode(mascotsOutAbout, true); }

		Defender defenders = null; //Creates object of nearest enemy
		for (int i = 0; i < 4; i++) { //If an enemy is in the play area
			if (nearestBadGuyLoc == AttackerList.get(i).getLocation()) {
				defenders = AttackerList.get(i);} }

		int distancetoNearestPlayer = 50; //distance of the nearest bad guy to the player
		if (nearestBadGuyLoc != null) { // If there are enemies near the player
			distancetoNearestPlayer = PlayerLoc.getPathDistance(nearestBadGuyLoc); }

		if (distancetoNearestPlayer > 50) { // If the "nearest" enemy is not that close to the player, go for the pills
			action = player.getNextDir(nearestLilPill, true); }
		else {
			if (distancetoNearestPlayer < 50) { if (distancetoNearestPlayer < 10) { // If the enemy(s) are close and then too close
					if (defenders.isVulnerable() && defenders.getVulnerableTime() > 5) {
						action = player.getNextDir(defenders.getLocation(), true); } // Goes after the enemy that is in the vulnerable state
					else {
						if (nearestBigPill == null) { //If there is zero power pills left on the map/level, have the player flee from the enemy
							action = player.getNextDir(defenders.getLocation(), false); }
						else {
							if (lengthfromnearestBigPill < 10 && player.getDirection() == defenders.getDirection()) { // Go after the power pill if a powerpill is near and the player and enemy are heading towards the same location
								action = player.getNextDir(nearestBigPill, true); }
							else { //Note: Space out code before submitting!
								action = player.getNextDir(defenders.getLocation(), false); } } } } else { //Flee from the enemy
					if (defenders.isVulnerable() && defenders.getVulnerableTime() > 5) { //If the enemy is in a vulnerable state and it is not too late, go after it
						action = player.getNextDir(defenders.getLocation(), true); } else if (nearestBigPill == null) { //Goes towards the enemy, and if there are no power pills go after the tiny pills.
						action = player.getNextDir(nearestLilPill, true); }
					else {
						action = player.getNextDir(nearestBigPill, true); } } } } //If there is powerpills, go after them
		return action;
	} // End Method
} // End Class
/*
		//An example (which should not be in your final submission) of some syntax that randomly chooses a direction for the attacker to move
		List<Integer> possibleDirs = game.getAttacker().getPossibleDirs(true);
		if (possibleDirs.size() != 0)
			action = possibleDirs.get(Game.rng.nextInt(possibleDirs.size()));
		else
			action = -1;

		//An example (which should not be in your final submission) of some syntax to use the visual debugging method, addPathTo, to the top left power pill.
		List<Node> powerPills = game.getPowerPillList();
		if (powerPills.size() != 0) {
			game.getAttacker().addPathTo(game, Color.BLUE, powerPills.get(0));
		}

		return action; */