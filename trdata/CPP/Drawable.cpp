#include "Drawable.h"

/* Podstawowa inicjalizacja obiektu wyświetlanego na ekranie,
 * przypisanie mu pierwszego z kolejki sprite.
 */
Drawable::Drawable(int xpos, int ypos, const std::vector<std::string>& spritesString, int size, int animationSpeed)
: animationSpeed(animationSpeed), size(size) {
	tf.add(spritesString);
	sprite.setTexture(tf.getFlip());
	setPosition(xpos, ypos);
}


/* Ta metoda jest wykorzystywana wszędzie tam, gdzie wyświetlany jest sprite,
 * czyli nie gdzie tekst czy inne takie! Nie ma opcji tworzenia obiektu bez animacji, bo
 * wymagałoby to wyświetlania go 'raz' i trzymania informacji, czy został
 * on wyświetlony, a tak jest prościej i niespecjalnie wolniej.
 */
void Drawable::draw(sf::RenderWindow& window) {
	if ((animationSpeedClock).getElapsedTime().asMilliseconds() > animationSpeed) {
		sprite.setTexture(tf.getFlip());
		animationSpeedClock.restart();
	}
	window.draw(sprite);
}
