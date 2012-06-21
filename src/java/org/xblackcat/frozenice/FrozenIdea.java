package org.xblackcat.frozenice;

import com.intellij.openapi.compiler.CompilerManager;
import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenice.config.IceConfig;
import org.xblackcat.frozenice.processor.Slice2Xxx;

/**
 * @author xBlackCat
 */
@State(
        name = "IceFramework",
        storages = {
                @Storage(
                        id = "default",
                        file = "$PROJECT_FILE$"
                ),
                @Storage(
                        id = "dir",
                        file = "$PROJECT_CONFIG_DIR$/projectIceFramework.xml",
                        scheme = StorageScheme.DIRECTORY_BASED
                )
        }
)
public class FrozenIdea implements BaseComponent, PersistentStateComponent<Element>, ProjectComponent {
    private IceConfig iceConfig;
    private final Project project;

    private Slice2Xxx translator;

    public FrozenIdea(Project project) {
        this.project = project;
    }

    public void initComponent() {
        translator = new Slice2Xxx();
        final CompilerManager compilerManager = CompilerManager.getInstance(project);
        compilerManager.addCompiler(translator);
        compilerManager.addCompilableFileType(IceFileType.INSTANCE);
    }

    public void disposeComponent() {
        final CompilerManager compilerManager = CompilerManager.getInstance(project);
        compilerManager.removeCompilableFileType(IceFileType.INSTANCE);
        compilerManager.removeCompiler(translator);
    }

    @NotNull
    public String getComponentName() {
        return "FrozenIdea";
    }

    @Override
    public void projectOpened() {
    }

    @Override
    public void projectClosed() {
    }

    @Override
    public Element getState() {
        final Element root = new Element("frozen-idea");

        Element frameWorksList = new Element("ice-frameworks");
        if (iceConfig != null && iceConfig.getFrameworkHomeUrl() != null) {
            Element fw = new Element("item");

            fw.setAttribute("url", iceConfig.getFrameworkHomeUrl());
            
            frameWorksList.addContent(fw);
        }

        root.addContent(frameWorksList);

        return root;
    }

    @Override
    public void loadState(Element state) {
        Element fwList = state.getChild("ice-frameworks");
        if (fwList != null) {
            Element fw = fwList.getChild("item");
            if (fw != null) {
                String url = fw.getAttributeValue("url");
                if (url != null) {
                    iceConfig = new IceConfig(url);
                }
            }
        }
    }

    public void setConfig(IceConfig iceConfig) {
        this.iceConfig = iceConfig;
    }

    public IceConfig getConfig() {
        return iceConfig;
    }
}
