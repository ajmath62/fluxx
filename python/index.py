import sys

from fluxx.deck import BASE_DECK
from fluxx.games import Game

if __name__ == '__main__':
    if len(sys.argv) > 1:
        num_players = int(sys.argv[1])
    else:
        num_players = 2

    game = Game(num_players, BASE_DECK)
    game.play()

