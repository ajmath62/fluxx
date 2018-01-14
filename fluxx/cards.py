from fluxx.enums import CardType, RuleType

class Card(object):
    
    def __init__(self, card_type):
        if not isinstance(card_type, CardType):
            raise TypeError('Must provide instance of CardType')
        self._type = card_type

    def __lt__(self, other):
        return self.__key < other.__key

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

    @property
    def __key(self):
        return self._type.value,


class Keeper(Card):

    def __init__(self, name):
        super().__init__(CardType.KEEPER)
        self._name = name
    
    def __repr__(self):
        return super().__repr__() + ', name {}'.format(self._name)

    def __str__(self):
        return super().__str__() + ': {}'.format(self._name)

    @property
    def name(self):
        return self._name

    @property
    def __key(self):
        return super().__key + self._name,


class Goal(Card):

    def __init__(self, name, keeper_names):
        # AJK TODO maybe put name back into the Card superclass
        super().__init__(CardType.GOAL)
        self._name = name
        self._keepers = tuple(keeper_names)
    
    def __repr__(self):
        return super().__repr__() + ', name {}, keepers {}'.format(self._name, ', '.join(self._keepers))

    def __str__(self):
        return super().__str__() + ': {}'.format(self._name)

    @property
    def name(self):
        return self._name

    @property
    def keepers(self):
        return self._keepers

    @property
    def __key(self):
        return super().__key + self._name,


class Rule(Card):

    def __init__(self, name, rule_type, rule_value):
        super().__init__(CardType.RULE)
        self._name = name
        if not isinstance(rule_type, RuleType):
            raise TypeError('Must provide instance of RuleType')
        self._rule_type = rule_type
        self._rule_value = rule_value
    
    def __repr__(self):
        return super().__repr__() + ', name {}'.format(self._name)

    def __str__(self):
        return super().__str__() + ': {}'.format(self._name)

    @property
    def name(self):
        return self._name

    @property
    def rule(self):
        return {'type': self._rule_type, 'value': self._rule_value}

    @property
    def __key(self):
        return super().__key + self._rule_type.value, self._rule_value

