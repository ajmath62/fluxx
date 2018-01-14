from random import shuffle

class Card(object):
    
    def __init__(self, card_type, name):
        self._type = card_type
        self._name = name

    def __repr__(self):
        return 'Card: type {}, name {}'.format(self.type, self.name)

    def __str__(self):
        return '{}: {}'.format(self.type, self.name)
    
    @staticmethod
    def list_repr(card_list, sep='\n  '):
        if not card_list:
            return sep + 'Empty'
        card_reprs = map(repr, card_list)
        return sep + sep.join(card_reprs)
    
    @property
    def type(self):
        return self._type
    
    @property
    def name(self):
        return self._name


class GameStatus(object):
    
    def __init__(self, players, deck):
        self.deck = list(deck)
        shuffle(self.deck)
        self.discard = []
        self.hands = [[] for _ in players]
        self.keepers = [[] for _ in players]
        self.goal = None
        self.rules = []
        self.populate_hands()
    
    def __repr__(self):
        deck_repr = 'Deck:' + Card.list_repr(self.deck)
        discard_repr = 'Discard:' + Card.list_repr(self.discard)
        goal_repr = 'Goal: ' + repr(self.goal)
        return 'Game status is as follows:\n{}\n{}\n{}'.format(deck_repr, discard_repr, goal_repr)
    
    def pop_from_deck(self):
        try:
            return self.deck.pop()
        except IndexError:  # deck is empty
            reshuffled_cards = self.discard[:-3]
            if not reshuffled_cards:
                raise RuntimeError('Deck is too small!')
            self.discard = self.discard[-3:]
            shuffle(reshuffled_cards)
            self.deck.extend(reshuffled_cards)
            return self.deck.pop()
    
    def populate_hands(self):
        num_players = self.num_players
        num_cards_to_deal = 3*num_players
        for n in range(num_cards_to_deal):
            card = self.pop_from_deck()
            self.hands[n % num_players].append(card)
    
    @property
    def num_players(self):
        return len(self.hands)


class Game(object):
    
    def __init__(self, n_players, deck):
        self._players = tuple(range(n_players))
        self._cards = tuple(deck)
        self._status = GameStatus(self._players, self._cards)
    
    def __repr__(self):
        card_repr = Card.list_repr(self._cards)
        game_repr = 'Game with {} players and {} cards:{}'.format(len(self._players), len(self._cards), card_repr)
        status_repr = repr(self._status)
        return game_repr + '\n' + status_repr


if __name__ == '__main__':
    deck = Card('Keeper', 'Chocolate'), Card('Keeper', 'Cookies'), Card('Keeper', 'Milk'), Card('Keeper', 'Death')
    game = Game(1, deck)
    print(game)

