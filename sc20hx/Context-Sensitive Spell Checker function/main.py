# -*- coding: utf-8 -*-

# Form implementation generated from reading ui file 'c:\Users\97142\Desktop\wordsTest\untitled.ui'
#
# Created by: PyQt5 UI code generator 5.9.2
#
# WARNING! All changes made in this file will be lost!

from urllib.request import ProxyBasicAuthHandler
from PyQt5 import QtCore, QtGui, QtWidgets
import sys
from PyQt5.QtWidgets import QApplication, QDialog, QLabel, QWidget
import tool
from PyQt5.QtGui import *
class Ui_Dialog(object):
    def setupUi(self, Dialog):
        Dialog.setObjectName("Dialog")
        Dialog.resize(643, 472)
        self.horizontalLayout_3 = QtWidgets.QHBoxLayout(Dialog)
        self.horizontalLayout_3.setObjectName("horizontalLayout_3")
        self.verticalLayout = QtWidgets.QVBoxLayout()
        self.verticalLayout.setObjectName("verticalLayout")
        self.textEdit = QtWidgets.QTextEdit(Dialog)
        self.textEdit.setObjectName("textEdit")
        self.verticalLayout.addWidget(self.textEdit)
        self.pushButton = QtWidgets.QPushButton(Dialog)
        self.pushButton.setObjectName("pushButton")
        self.verticalLayout.addWidget(self.pushButton)
        self.horizontalLayout_3.addLayout(self.verticalLayout)
        self.verticalLayout_2 = QtWidgets.QVBoxLayout()
        self.verticalLayout_2.setObjectName("verticalLayout_2")
        self.horizontalLayout_3.addLayout(self.verticalLayout_2)
        self.retranslateUi(Dialog)
        QtCore.QMetaObject.connectSlotsByName(Dialog)

    def clearLayout(self, layout):
        if layout is not None:
            while layout.count():
                item = layout.takeAt(0)
                widget = item.widget()
                if widget is not None:
                    widget.deleteLater()
                else:
                    self.clearLayout(item.layout())

    def add(self):   
        try:
            layconts = self.verticalLayout_2.children()
            for layout in layconts:
                self.clearLayout(layout)
        except:
            pass
        text = ''
        text = self.textEdit.toPlainText() 
        print(text)
        matchs = tool.get_matchs(text)
        #print(matchs)

        if len(matchs) == 0 :
            QtWidgets.QMessageBox.information(self.pushButton,'text','no mistakes were found')
        else:
            for item in matchs :
                print(item)
                start = item.offset
                end = item.errorLength
                word = text[start : start + end]
                print(word)
                document = self.textEdit.document()
                highlight_cursor = QTextCursor(document)
                cursor_2 = QTextCursor(document)
                cursor_2.beginEditBlock()
                color_format = QTextCharFormat(highlight_cursor.charFormat())
                color_format.setBackground(QColor(255, 211, 6))
                while (not highlight_cursor.isNull()) and (not highlight_cursor.atEnd()):
                    highlight_cursor = document.find(word, highlight_cursor)
                    if not highlight_cursor.isNull():
                        highlight_cursor.mergeCharFormat(color_format)
                cursor_2.endEditBlock()

                try : 
                    laycont = self.horizontalLayout.count()
                except:
                    laycont = 1
                name = "horizontalLayout" + str(laycont)
                self.horizontalLayout = QtWidgets.QHBoxLayout()
                self.horizontalLayout.setObjectName(name)

                self.label = QtWidgets.QLabel()
                name = "label" + str(laycont)
                self.label.setObjectName(name)
                self.horizontalLayout.addWidget(self.label)
                self.label.setText(item.message)

                self.line = QtWidgets.QFrame()
                self.line.setFrameShape(QtWidgets.QFrame.VLine)
                self.line.setFrameShadow(QtWidgets.QFrame.Sunken)
                name = "line" + str(laycont)
                self.line.setObjectName(name)
                self.horizontalLayout.addWidget(self.line)

                self.comboBox = QtWidgets.QComboBox()
                self.comboBox.setObjectName("comboBox")
                self.comboBox.addItems(item.replacements)
                self.horizontalLayout.addWidget(self.comboBox)
                self.horizontalLayout.setStretch(0, 1)
                self.horizontalLayout.setStretch(2, 1)
                self.verticalLayout_2.addLayout(self.horizontalLayout)
                #self.textEdit.setText('<font color="red">' + word + '</font>')
            matchs = []

      

    def retranslateUi(self, Dialog):
        _translate = QtCore.QCoreApplication.translate
        Dialog.setWindowTitle(_translate("Dialog", "Dialog"))
        self.textEdit.setText('Please enter your sentence .')
        self.textEdit.setLineWrapMode(QtWidgets.QTextEdit.WidgetWidth)
        self.pushButton.setText(_translate("Dialog", "Check"))
        #self.label_2.setText(_translate("Dialog", "Error Type"))
        #self.label.setText(_translate("Dialog", "Error Type"))
        self.pushButton.clicked.connect(self.add)


if __name__ =="__main__":
    print("context-sensitive spell checker")
    import sys
    app=QtWidgets.QApplication(sys.argv)
    mainWindowOriginal=QtWidgets.QDialog()
    second_ui=Ui_Dialog()
    second_ui.setupUi(mainWindowOriginal)
    mainWindowOriginal.show()
    sys.exit(app.exec_())
    ##Hollo world