#include "PowerMissileIndicator.h"

PowerMissileIndicator::PowerMissileIndicator() : rect(sf::Vector2f(100, 20)) {
	setPosition(0, 30);
}

// W zależności od stosunku czasów: tego, który upłynął i limitu wystrzeleń,
// ustawiamy odpowiednio jaskrawy kolor prostokąta
void PowerMissileIndicator::update() {
	// kolor do którego dążymy: ff6600
	sf::Color col;

	double factor = ((double)PowerMissile::missileLimitClock.getElapsedTime().asMilliseconds()) / ((double)PowerMissile::timeLimit);
	if (factor > 1.0) factor = 1.0;

	col.r = factor * 0xff; // skalowanie
	col.g = factor * 0x66;
	col.b = factor * 0x00;

	rect.setFillColor(sf::Color(col)); // wtf I meant here?
}
