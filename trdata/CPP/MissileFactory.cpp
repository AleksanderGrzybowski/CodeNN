#include "MissileFactory.h"

Missile* MissileFactory::getRandomMissile(int shipx, int shipy) {
	if (Random::tryChance(50)) return new NormalMissile(shipx, shipy);
	else return new PowerMissile(shipx, shipy);
}
