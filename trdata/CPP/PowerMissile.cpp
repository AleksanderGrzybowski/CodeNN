#include "PowerMissile.h"

PowerMissile::PowerMissile(int shipx, int shipy)
	: Missile(shipx, shipy, {"Sprites/PowerMissileA.png", "Sprites/PowerMissileB.png"}, 20, 100, 0.5, 20)
{}

bool PowerMissile::canBeSent() {
	return (PowerMissile::missileLimitClock).getElapsedTime().asMilliseconds() > PowerMissile::timeLimit;
}

void PowerMissile::restartClock() {
	missileLimitClock.restart();
}

sf::Clock PowerMissile::missileLimitClock;
const int PowerMissile::timeLimit = 1000;
