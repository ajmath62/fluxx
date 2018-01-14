from enum import Enum, auto


class AutoEnum(Enum):
    def __repr__(self):
        return '<{}.{}>'.format(self.__class__.__name__, self.name)


class CardType(Enum):
    KEEPER = 'Keeper'
    GOAL = 'Goal'
    RULE = 'New Rule'


class RuleType(AutoEnum):
    PLAY = auto()
    DRAW = auto()
    HAND_LIMIT = auto()
    KEEPER_LIMIT = auto()


class GameStage(AutoEnum):
    START_GAME = auto()
    START_TURN = auto()
    START_PLAY = auto()
    PLAY = auto()
    END_PLAY = auto()
    END_TURN = auto()
    END_GAME = auto()

