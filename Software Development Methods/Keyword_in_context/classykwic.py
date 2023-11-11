class Kwic:

    def __init__(self, excluded, lines):
        self.excluded = []
        self.index_lines = []
        #print(excluded)
        #print(lines)
        keywords = self.get_keywords(lines, excluded)
        #print(keywords)
        keywords_ = sorted(set(keywords), key = lambda x:keywords.index(x))
        keywords_.sort(key = str.lower)
        self.sort_phrases(keywords_, lines)
        #print(keywords)
        #self.formatted_lines = []

	#changes the phrases and exclusion words to lowercase and then removes the exclusion words
	#to determine which are keywords and then extends a list with the found keywords.
    def get_keywords(self, lines, excluded):
        keywords = []
        for i in range(len(lines)):
            phrase = lines[i].split(',')
            words = phrase[0].split(' ')
            lowercase_words = [x.lower() for x in words]
            excludes = [x.lower() for x in excluded]
            for j in range(len(excluded)):
                if(excludes[j] in lowercase_words):
                    #https://stackoverflow.com/questions/2186656/how-can-i-remove-all-instances-of-an-element-from-a-list-in-python
                    lowercase_words[:] = [x for x in lowercase_words if x != excludes[j]]
            keywords.extend(lowercase_words)

        #print(keywords)
        return(keywords)

    def sort_phrases(self, keywords, phrases):
        '''searches each phrase for each sorted keyword'''
        for i in range(len(keywords)):
            key = keywords[i]
            for j in range(len(phrases)):
                original_phrase = phrases[j].split()
                lowercase_phrase = [x.lower() for x in original_phrase]
                if(key in lowercase_phrase):
                    find_keyword = lowercase_phrase
                    ind = find_keyword.index(keywords[i]) #finds the index of the keyword

                    '''resizes the left string if it is too long'''
                    left = original_phrase[0:ind]
                    if(len(' '.join(left)) > 19): #if the left string goes past column 10 then we need to remove some words
                        size = (len(' '.join(left)))
                        while(size > 19):
                            left.pop(0) #removing a word from the start of the left list
                            size = len(' '.join(left))
                        final_left = ' '.join(left)
                    else:
                        final_left = ' '.join(left)

                    '''resizes the right string if it is too long'''
                    right = original_phrase[ind + 1:]
                    if(len(' '.join(right)) + len(key) > 30): #if right string goes past column 60 then we need to remove some words
                        size = (len(' '.join(right)) + len(key))
                        while(size > 30):
                            right.pop(-1) #removing a word from the end of the right list
                            size = (len(' '.join(right)) + len(key))
                        final_right = ' '.join(right).strip()
                    else:
                        final_right = ' '.join(right).strip()

                    #formatted_lines = []
                    '''formatting the print statements to allign the keyword on column 30, and justifies the left and right strings to the keyword'''
                    if(not final_right):#the case if there is no string after the keyword
                        self.index_lines.append('{:>28} {:<}'.format(final_left, find_keyword[ind].upper()))
                    else:#the case for when there is a string after the keyword
                        self.index_lines.append('{:>28} {:} {:<}'.format(final_left, find_keyword[ind].upper(), final_right))


    def output(self):
        #formatted_lines = []
        return self.index_lines #this line was just '[]' before replacing with formatted_lines
