package ru.karvozavr.plugin.astinfo.services

import com.intellij.codeInsight.ExceptionUtil
import com.intellij.psi.JavaElementVisitor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceExpression
import com.intellij.psi.PsiVariable
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.PsiUtil
import ru.karvozavr.plugin.astinfo.ASTInfoData
import ru.karvozavr.plugin.astinfo.ASTInfoModel
import ru.karvozavr.plugin.astinfo.toolwindow.ASTInfoToolWindow

class ASTInfoServiceImpl : AstInfoService {

    var window: ASTInfoToolWindow? = null

    override fun registerASTInfoToolWindow(window: ASTInfoToolWindow) {
        this.window = window
    }

    override fun astInfoBySelection(beginElement: PsiElement, endElement: PsiElement) {
        val selectionContext = extractContextBySelection(beginElement, endElement)
        val info = getInfoFromElements(selectionContext)
        window?.updateView(ASTInfoModel(selectionContext, info))
    }

    private fun getInfoFromElements(elements: List<PsiElement>): ASTInfoData {
        val visitor = ASTVariableInfoVisitor()
        val elementsArray = elements.toTypedArray()
        PsiTreeUtil.processElements({ it.accept(visitor); true }, elementsArray)
        val exceptions = ExceptionUtil.getThrownExceptions(elementsArray).size
        return ASTInfoData(visitor.declarations, visitor.usages, exceptions)
    }

    private fun extractContextBySelection(beginElement: PsiElement, endElement: PsiElement): List<PsiElement> {
        return if (beginElement == endElement) {
            listOf(beginElement)
        } else {
            val selectionContext = PsiTreeUtil.findCommonContext(beginElement, endElement)!!
            trimBlockToSelection(selectionContext, beginElement, endElement)
        }
    }

    private fun trimBlockToSelection(element: PsiElement, beginElement: PsiElement, endElement: PsiElement): List<PsiElement> {
        val begin = PsiTreeUtil.findFirstParent(beginElement) { PsiTreeUtil.getDepth(it, element) == 1 }
        val end = PsiTreeUtil.findFirstParent(endElement) { PsiTreeUtil.getDepth(it, element) == 1 }
        val from = element.children.indexOf(begin)
        val to = element.children.indexOf(end)
        return if (from == 0 && to == element.children.size - 1) {
            listOf(element)
        } else {
            element.children.slice(from..to)
        }
    }

    private class ASTVariableInfoVisitor : JavaElementVisitor() {

        var declarations = 0
        var usages = 0

        override fun visitVariable(variable: PsiVariable) {
            super.visitVariable(variable)
            declarations++
        }

        override fun visitReferenceExpression(expression: PsiReferenceExpression) {
            super.visitReferenceExpression(expression)
            if (PsiUtil.isAccessedForReading(expression)) {
                usages++
            }
        }
    }
}