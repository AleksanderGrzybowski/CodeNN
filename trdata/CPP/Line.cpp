#include "Line.h"

Line::Line() { // korzystamy z bezargumentowego ctora Drawable
	// Musimy tutaj oporządzić teksturę, bo wycinamy z niej prostokąt szeroki jak ekran
	// Z każdej tekstury w getSpritesString() wycinamy prostokąt

	for (const std::string& s: {"Sprites/Line.png"}) {
		sf::Texture texture;
		texture.loadFromFile(s, sf::IntRect(0, 0, CONF_screenWidth, CONF_lineHeight));
		texture.setRepeated(true);
		tf.add(texture); // przyjmowana jako referencja, potem jest kopiowana oczywiście
	}

	setPosition(0, CONF_screenHeight*(1-CONF_shipUpLimit));
}
