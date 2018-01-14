from enum import Enum, auto
from random import shuffle

from cards.cards import Card, CardType, Goal, Keeper


class GameStatus(object):
    
    def __init__(self, players, deck):
        self.deck = list(deck)
        shuffle(self.deck)
        self.discard = []
        self._hands = [set() for _ in players]
        self._keepers = [set() for _ in players]
        self.goal = None
        self.rules = set()
        self.populate_hands()
        self.current_player = players[0]
        self.cards_drawn = 0
        self.cards_played = 0
    
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

    def draw_card(self, player):
        card = self.pop_from_deck()
        self._hands[player].add(card)

    def list_hand(self, player):
        return sorted(self._hands[player])

    def list_keepers(self, player):
        return sorted(self._keepers[player])

    def play_goal(self, player, card):
        self._hands[player].remove(card)
        old_goal = self.goal
        if old_goal:
            self.discard.append(old_goal)
        self.goal = card

    def play_keeper(self, player, card):
        self._hands[player].remove(card)
        self._keepers[player].add(card)

    def populate_hands(self):
        num_players = self.num_players
        num_cards_to_deal = 3*num_players
        for n in range(num_cards_to_deal):
            self.draw_card(n % num_players)
    
    @property
    def num_players(self):
        return len(self._hands)

    @property
    def num_draw(self):
        return 1

    @property
    def num_play(self):
        return 1


class GameStage(Enum):
    START_GAME = auto()
    START_TURN = auto()
    START_PLAY = auto()
    PLAY = auto()
    END_PLAY = auto()
    END_TURN = auto()
    END_GAME = auto()

    def __repr__(self):
        return '<{}.{}>'.format(self.__class__.__name__, self.name)


class Game(object):
    
    def __init__(self, n_players, deck):
        self._players = tuple(range(n_players))
        self._winner = None
        self._cards = tuple(deck)
        self._status = GameStatus(self._players, self._cards)
        self._stage = GameStage.START_GAME
    
    def __repr__(self):
        card_repr = Card.list_repr(self._cards)
        game_repr = 'Game with {} players and {} cards:{}'.format(len(self._players), len(self._cards), card_repr)
        status_repr = repr(self._status)
        return game_repr + '\n' + status_repr

    def draw_card(self, player):
        num_draw = self._status.num_draw
        for _ in range(num_draw):
            self._status.draw_card(player)

    def play_card(self, player, card):
        if card.type is CardType.KEEPER:
            self._status.play_keeper(player, card)
        elif card.type is CardType.GOAL:
            self._status.play_goal(player, card)
        else:
            raise TypeError('Invalid card type')
    
    def step(self):
        current_player = self._status.current_player
        hand = sorted(self._status.list_hand(current_player))
        keepers = sorted(self._status.list_keepers(current_player))
        goal = self._status.goal
        if self._stage is GameStage.START_GAME:
            # There will be stuff here later; right now it's all in __init__.
            print('Time for a new game...')
            self._stage = GameStage.START_TURN
        elif self._stage is GameStage.START_TURN:
            print('Starting your turn')
            self.draw_card(current_player)
            self._stage = GameStage.START_PLAY
        elif self._stage is GameStage.START_PLAY:
            print('Your hand is:', Card.list_repr(hand, func=str, number=True))
            print('Your keepers are:', Card.list_repr(keepers, func=str, number=False))
            print('The goal is:', goal.name)
            self._stage = GameStage.PLAY
        elif self._stage is GameStage.PLAY:
            card_index = int(input('Which number card would you like to play? ')) - 1  # because 1-indexed
            card = hand[card_index]
            self.play_card(current_player, card)
            self._stage = GameStage.END_PLAY
        elif self._stage is GameStage.END_PLAY:
            if goal is not None:
                keeper_names = goal.keepers
                for player in self._players:
                    player_hand = {c.name for c in self._status.list_keepers(player)}
                    if all(k in player_hand for k in keeper_names):
                        self._winner = player
                        self._stage = GameStage.END_GAME
                        return
            self._status.cards_played += 1
            if self._status.cards_played >= self._status.num_play:
                self._stage = GameStage.END_TURN
            else:
                self._stage = GameStage.START_PLAY
        elif self._stage is GameStage.END_TURN:
            next_player = (current_player + 1) % len(self._players)
            self._status.current_player = next_player
            self._stage = GameStage.START_TURN
        elif self._stage is GameStage.END_GAME:
            print('The winner is player {}!'.format(self._winner + 1))
            print('Good game!')
            raise GeneratorExit
        else:
            raise NotImplementedError

    def play(self):
        while True:
            try:
                self.step()
            except GeneratorExit:
                break


if __name__ == '__main__':
    keepers = tuple(Keeper(e) for e in ('Chocolate', 'Cookies', 'Milk', 'Death', 'War', 'Taxes', 'Peace', 'Love', 'Coffee', 'Doughnuts', 'Sun', 'Moon', 'Rocket', 'Eye', 'Pyramid'))
    goals = tuple(Goal(n, k) for n, k in (('Great Seal', ('Eye', 'Pyramid')), ('Hippyism', ('Peace', 'Love')), ('Latte', ('Coffee', 'Milk'))))
    deck = keepers + goals
    game = Game(2, deck)
    game.play()

