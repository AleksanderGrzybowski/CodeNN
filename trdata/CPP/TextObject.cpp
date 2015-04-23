#include "TextObject.h"

TextObject::TextObject(int xpos, int ypos, int fontSize, sf::Color color, const std::string& str)
	: str(str), xpos(xpos), ypos(ypos), fontSize(fontSize), color(color) {

	if (!fileExists(CONF_fontFile)) throw FileNotFoundException(CONF_fontFile);
	font.loadFromFile(CONF_fontFile);
}

// Każde draw aktualizuje zawartość
void TextObject::draw(sf::RenderWindow& window) {
	text.setFont(font);
	text.setString(str);
	text.setPosition(xpos, ypos);
	text.setCharacterSize(fontSize);
	text.setColor(color);

	window.draw(text);
}
