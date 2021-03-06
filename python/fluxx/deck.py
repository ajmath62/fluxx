from fluxx import cards
from fluxx.enums import RuleType


_keepers = tuple(cards.Keeper(e) for e in (
    'Chocolate', 'Cookies', 'Milk', 'Death', 'War', 'Taxes', 'Peace', 'Love', 
    'Coffee', 'Doughnuts', 'Sun', 'Moon', 'Rocket', 'Eye', 'Pyramid',
))
_goals = tuple(cards.Goal(n, (k1, k2)) for n, k1, k2 in (
    ('Great Seal', 'Eye', 'Pyramid'), ('Hippyism', 'Peace', 'Love'), ('Latte', 'Coffee', 'Milk'),
    ('Luminaries', 'Sun', 'Moon'), ('Tolstoy', 'War', 'Peace'), ('Woody Allen', 'Love', 'Death'),
    ('Apollo 11', 'Rocket', 'Moon'), ('Breakfast', 'Coffee', 'Doughnuts')
))
_play_rules = tuple(cards.Rule('Play {}'.format(v), RuleType.PLAY, v) for v in (1, 2, 3, 4))
_draw_rules = tuple(cards.Rule('Draw {}'.format(v), RuleType.DRAW, v) for v in (1, 2, 3, 4, 5))
_hand_limit_rules = tuple(cards.Rule('Hand Limit {}'.format(v), RuleType.HAND_LIMIT, v) for v in (0, 1, 4))
_keeper_limit_rules = tuple(cards.Rule('Keeper Limit {}'.format(v), RuleType.KEEPER_LIMIT, v) for v in (2, 3, 4))
_rules = _play_rules + _draw_rules + _hand_limit_rules + _keeper_limit_rules
BASE_DECK = _keepers + _goals + _rules

