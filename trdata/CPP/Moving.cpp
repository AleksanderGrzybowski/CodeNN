#include "Moving.h"

/* Główna metoda do iteracyjnego poruszania obiektów */
void Moving::moveIterate(const sf::Time& t) {
	double toMove = speed*t.asMilliseconds();

	switch (dir) {
	case Up:
		move(0, -toMove);
		break;
	case Down:
		move(0, toMove);
		break;
	case Left:
		move(-toMove, 0);
		break;
	case Right:
		move(toMove, 0);
		break;
	}

}
