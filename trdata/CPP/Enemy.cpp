#include "Enemy.h"

Enemy::Enemy(int xpos, int ypos, std::vector<std::string> spritesString, int size,
	int animationSpeed, double speed, int health, int pointsReceived)
	: Moving(xpos, ypos, spritesString, size, animationSpeed, speed, Down), health(health), pointsReceived(pointsReceived)
{}

