#include "TextureFlipper.h"

/* Unikamy realokacji, zresztą kto by więcej tekstur chciał? :) */
TextureFlipper::TextureFlipper() : next(0) {
	tab.reserve(10);
}

/* Dodanie pojedynczej tekstury już wczytanej z pliku */
void TextureFlipper::add(const sf::Texture& t) {
	tab.push_back(t);
}

/* Załadowanie tekstur z pliku/plików */
void TextureFlipper::add(const std::vector<std::string>& v) {
	sf::Texture t;

	for (const std::string& s : v) {
		if (!fileExists(s)) throw FileNotFoundException(s);
		t.loadFromFile(s);
		tab.push_back(t);
	}
}

/* Element zwracany przez const referencję, bezpiecznie */
const sf::Texture& TextureFlipper::getFlip() {
	const sf::Texture& ret = tab[next];
	next++;
	if (next == tab.size()) next = 0; // implementacja - lista cykliczna na tablicy
	return ret;
}
