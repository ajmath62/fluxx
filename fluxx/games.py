from random import shuffle

from fluxx.cards import Card, Goal, Keeper, Rule
from fluxx.enums import CardType, GameStage, RuleType


class GameStatus(object):
    
    def __init__(self, players, deck):
        self.deck = list(deck)
        shuffle(self.deck)
        self._discard = []
        self._hands = [set() for _ in players]
        self._keepers = [set() for _ in players]
        self.goal = None
        self.rules = {}
        self.populate_hands()
        self.current_player = players[0]
        self.cards_drawn = 0
        self.cards_played = 0
    
    def __repr__(self):
        deck_repr = 'Deck:' + Card.list_repr(self.deck)
        discard_repr = 'Discard:' + Card.list_repr(self._discard)
        goal_repr = 'Goal: ' + repr(self.goal)
        return 'Game status is as follows:\n{}\n{}\n{}'.format(deck_repr, discard_repr, goal_repr)
    
    def pop_from_deck(self):
        try:
            return self.deck.pop()
        except IndexError:  # deck is empty
            reshuffled_cards = self._discard[:-3]
            if not reshuffled_cards:
                raise RuntimeError('Deck is too small!')
            print('Shuffling deck...')
            self._discard = self._discard[-3:]
            shuffle(reshuffled_cards)
            self.deck.extend(reshuffled_cards)
            return self.deck.pop()

    def discard(self, player, card):
        self._hands[player].remove(card)
        self._discard.append(card)

    def discard_keeper(self, player, card):
        self._keepers[player].remove(card)
        self._discard.append(card)

    def draw_card(self, player):
        card = self.pop_from_deck()
        self._hands[player].add(card)

    def list_hand(self, player):
        return sorted(self._hands[player])

    def list_keepers(self, player):
        return sorted(self._keepers[player])

    def play_card(self, player, card):
        if card.type is CardType.KEEPER:
            self._hands[player].remove(card)
            self._keepers[player].add(card)
        elif card.type is CardType.GOAL:
            self._hands[player].remove(card)
            old_goal = self.goal
            if old_goal:
                self._discard.append(old_goal)
            self.goal = card
        elif card.type is CardType.RULE:
            self._hands[player].remove(card)
            rule_type = card.rule['type']
            old_rule = self.rules.get(rule_type, None)
            if old_rule:
                self._discard.append(old_rule)
            self.rules[rule_type] = card
        else:
            raise TypeError('Invalid card type')

    def populate_hands(self):
        num_players = self.num_players
        num_cards_to_deal = 3*num_players
        for n in range(num_cards_to_deal):
            self.draw_card(n % num_players)

    def rules_repr(self, sep='\n  '):
        draw_repr = 'Draw {}'.format(self.num_draw)
        play_repr = 'Play {}'.format(self.num_play)
        hl_repr = 'Hand Limit {}'.format(self.hand_limit) if self.hand_limit is not None else 'No Hand Limit'
        kl_repr = 'Keeper Limit {}'.format(self.keeper_limit) if self.keeper_limit is not None else 'No Keeper Limit'
        return sep + sep.join((draw_repr, play_repr, hl_repr, kl_repr))
    
    @property
    def num_players(self):
        return len(self._hands)

    @property
    def num_draw(self):
        draw_rule = self.rules.get(RuleType.DRAW, None)
        if draw_rule:
            return draw_rule.rule['value']
        else:
            return 1

    @property
    def left_to_draw(self):
        return max(self.num_draw - self.cards_drawn, 0)

    @property
    def num_play(self):
        play_rule = self.rules.get(RuleType.PLAY, None)
        if play_rule:
            return play_rule.rule['value']
        else:
            return 1

    @property
    def left_to_play(self):
        return max(self.num_play - self.cards_played, 0)

    @property
    def hand_limit(self):
        hl_rule = self.rules.get(RuleType.HAND_LIMIT, None)
        if hl_rule:
            return hl_rule.rule['value']
        else:
            return None

    @property
    def keeper_limit(self):
        kl_rule = self.rules.get(RuleType.KEEPER_LIMIT, None)
        if kl_rule:
            return kl_rule.rule['value']
        else:
            return None


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

    def draw_cards(self, player):
        num_draw = self._status.left_to_draw
        for _ in range(num_draw):
            self._status.draw_card(player)
        self._status.cards_drawn += num_draw

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
            print('\033[97m' + 'Starting your turn' + '\033[0m')
            self._status.cards_drawn = 0
            self._status.cards_played = 0
            self.draw_cards(current_player)
            self._stage = GameStage.START_PLAY
        elif self._stage is GameStage.START_PLAY:
            print('Your hand is:', Card.list_repr(hand, func=str, number=True))
            print('Your keepers are:', Card.list_repr(keepers, func=str, number=False))
            print('The rules are:', '\033[{}m'.format(Rule.color), self._status.rules_repr(), '\033[0m')
            print('The goal is:', goal.color_str + goal.name if goal else None, '\033[0m')
            self._stage = GameStage.PLAY
        elif self._stage is GameStage.PLAY:
            while True:
                card_index = int(input('Which number card would you like to play? ')) - 1  # because 1-indexed
                try:
                    card = hand[card_index]
                except IndexError:
                    print('\033[31m' + 'Invalid card.' + '\033[0m')
                    continue
                else:
                    break
            self._status.play_card(current_player, card)
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
            self.draw_cards(current_player)
            if self._status.left_to_play and self._status.list_hand(current_player):
                self._stage = GameStage.START_PLAY
            else:
                self._stage = GameStage.END_TURN
        elif self._stage is GameStage.END_TURN:
            hand_limit = self._status.hand_limit
            keeper_limit = self._status.keeper_limit
            if hand_limit is not None and len(hand) > hand_limit:
                if hand_limit == 0:
                    print('\033[31m' + 'You are over the hand limit. All your cards are being discarded.', '\033[0m')
                    for card in hand:
                        self._status.discard(current_player, card)
                else:
                    print('\033[31m' + 'You are over the hand limit. Please choose cards to discard.', '\033[0m')
                    print('Your hand is:', Card.list_repr(hand, func=str, number=True))
                    card_index = int(input('Which number card would you like to discard? ')) - 1
                    card = hand[card_index]
                    self._status.discard(current_player, card)
                self._stage = GameStage.END_TURN
            elif keeper_limit is not None and len(keepers) > keeper_limit:
                print('\033[31m' + 'You are over the keeper limit. Please choose keepers to discard.', '\033[0m')
                print('Your keepers are:', Card.list_repr(keepers, func=str, number=True))
                card_index = int(input('Which number keeper would you like to discard? ')) - 1
                card = keepers[card_index]
                self._status.discard_keeper(current_player, card)
                self._stage = GameStage.END_TURN
            else:
                next_player = (current_player + 1) % len(self._players)
                self._status.current_player = next_player
                print('\033[97m' + 'End of turn' + '\033[0m')
                self._stage = GameStage.START_TURN
        elif self._stage is GameStage.END_GAME:
            print('\033[96m' + 'The winner is player {}!'.format(self._winner + 1))
            print('Good game!', '\033[0m')
            raise GeneratorExit
        else:
            raise NotImplementedError

    def play(self):
        while True:
            try:
                self.step()
            except GeneratorExit:
                break

