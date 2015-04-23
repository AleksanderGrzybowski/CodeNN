#include "Missile.h"

Missile::Missile(int shipx, int shipy, const std::vector<std::string>& spritesString, int size,
	int animationSpeed, double speed, int damage)
	: Moving(shipx + CONF_shipSize/2 - (size/2.0), shipy, spritesString, size, animationSpeed, speed, Up),
	  damage(damage)
{ }

