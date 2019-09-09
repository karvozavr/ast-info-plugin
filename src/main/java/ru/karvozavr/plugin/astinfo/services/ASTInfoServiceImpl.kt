package ru.karvozavr.plugin.astinfo.services

import com.intellij.psi.JavaElementVisitor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceExpression
import com.intellij.psi.PsiVariable
import com.intellij.psi.util.PsiTreeUtil
import ru.karvozavr.plugin.astinfo.ASTInfoData
import ru.karvozavr.plugin.astinfo.ASTInfoModel
import ru.karvozavr.plugin.astinfo.toolwindow.ASTInfoToolWindow

class ASTInfoServiceImpl : AstInfoService {

    var window: ASTInfoToolWindow? = null

    override fun registerASTInfoToolWindow(window: ASTInfoToolWindow) {
        this.window = window
    }

    override fun astInfoBySelection(beginElement: PsiElement, endElement: PsiElement) {
        val psiElements = extractContextBySelection(beginElement, endElement)
        val info = getInfoFromElements(psiElements)
        window?.updateView(ASTInfoModel(psiElements, info))
    }

    private fun getInfoFromElements(elements: List<PsiElement>): ASTInfoData {
        val visitor = object : JavaElementVisitor() {
            var declarations = 0
            var usages = 0
            var exceptions = 0

            override fun visitVariable(variable: PsiVariable?) {
                super.visitVariable(variable)
                declarations++
            }

            override fun visitReferenceExpression(expression: PsiReferenceExpression?) {
                super.visitReferenceExpression(expression)
                usages++
            }
        }
        PsiTreeUtil.processElements({ it.accept(visitor); true }, elements.toTypedArray())
        return ASTInfoData(visitor.declarations, visitor.usages, visitor.exceptions)
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
}