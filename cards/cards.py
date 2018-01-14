from enum import Enum

class CardType(Enum):
    KEEPER = 'Keeper'

    def __repr__(self):
        return '<{}.{}>'.format(self.__class__.__name__, self.name)


class Card(object):
    
    def __init__(self, card_type):
        if not isinstance(card_type, CardType):
            raise TypeError('Must provide instance of CardType')
        self._type = card_type

    def __lt__(self, other):
        return self._type.value < other._type.value

    def __repr__(self):
        return 'Card: type {}'.format(self._type.value)

    def __str__(self):
        return '{}'.format(self._type.value)
    
    @staticmethod
    def list_repr(card_list, func=repr, sep='\n  ', number=False):
        if not card_list:
            return sep + 'Empty'
        card_reprs = map(func, card_list)
        if number:
            numbering = range(1, len(card_list)+1)
            zipped_reprs = zip(numbering, card_reprs)
            card_reprs = ('{}. {}'.format(*e) for e in zipped_reprs)
        return sep + sep.join(card_reprs)

    @property
    def type(self):
        return self._type


class Keeper(Card):

    def __init__(self, name):
        super().__init__(CardType.KEEPER)
        self._name = name
    
    def __lt__(self, other):
        if self._type is other._type:
            return self._name < other._name
        else:
            return super().__lt__(other)

    def __repr__(self):
        return super().__repr__() + ', name {}'.format(self._name)

    def __str__(self):
        return super().__str__() + ': {}'.format(self._name)

    @property
    def name(self):
        return self._name

