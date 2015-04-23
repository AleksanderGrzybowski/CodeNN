#include "NormalEnemy.h"

NormalEnemy::NormalEnemy(double xpos, double ypos)
	: Enemy(xpos, ypos, {"Sprites/NormalEnemy.png"}, 64, 100, 0.05, 5, 1)
{ }
