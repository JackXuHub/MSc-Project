import language_tool_python


## define a main function to check the sentences.
def get_matchs(text):
    tool = language_tool_python.LanguageTool('en-US')
    matches = tool.check(text)
    #print(matches)
    return matches

if __name__ == "__main__":
    text = "hollo"
    get_matchs(text)
