package ru.karvozavr.plugin.astinfo.services

import com.intellij.psi.PsiElement
import ru.karvozavr.plugin.astinfo.toolwindow.ASTInfoToolWindow

interface AstInfoService {

    fun registerASTInfoToolWindow(window: ASTInfoToolWindow)

    fun astInfoBySelection(beginElement: PsiElement, endElement: PsiElement)
}