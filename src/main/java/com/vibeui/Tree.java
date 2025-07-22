package com.vibeui;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.function.Consumer;

public class Tree extends Component<Tree> {
    private JTree tree;
    private DefaultTreeModel treeModel;
    private DefaultMutableTreeNode rootNode;
    private JScrollPane scrollPane;
    private Consumer<TreeNode> selectionHandler;
    private Consumer<TreeNode> doubleClickHandler;
    private Consumer<TreeNode> expandHandler;
    private Consumer<TreeNode> collapseHandler;

    private Tree(TreeNode rootNode) {
        super(createScrollableTree(rootNode));
        initializeComponents();
        setupEventHandlers();
    }

    private static JScrollPane createScrollableTree(TreeNode rootNode) {
        DefaultMutableTreeNode root = createSwingNode(rootNode);
        DefaultTreeModel model = new DefaultTreeModel(root);
        JTree tree = new JTree(model);
        return new JScrollPane(tree);
    }

    private static DefaultMutableTreeNode createSwingNode(TreeNode node) {
        DefaultMutableTreeNode swingNode = new DefaultMutableTreeNode(node);
        
        for (TreeNode child : node.getChildren()) {
            swingNode.add(createSwingNode(child));
        }
        
        return swingNode;
    }

    private void initializeComponents() {
        this.scrollPane = (JScrollPane) swingComponent;
        this.tree = (JTree) scrollPane.getViewport().getView();
        this.treeModel = (DefaultTreeModel) tree.getModel();
        this.rootNode = (DefaultMutableTreeNode) treeModel.getRoot();
    }

    public static Tree create(TreeNode rootNode) {
        return new Tree(rootNode);
    }

    public static Tree createEmpty(String rootTitle) {
        TreeNode root = TreeNode.create(rootTitle);
        return new Tree(root);
    }

    @Override
    protected void configureDefaults() {
        super.configureDefaults();
    }

    private void setupEventHandlers() {
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                
                if (selectedNode != null && selectionHandler != null) {
                    TreeNode treeNode = (TreeNode) selectedNode.getUserObject();
                    selectionHandler.accept(treeNode);
                }
            }
        });

        // Add mouse listener for double-clicks
        tree.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2 && doubleClickHandler != null) {
                    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                    if (selectedNode != null) {
                        TreeNode treeNode = (TreeNode) selectedNode.getUserObject();
                        doubleClickHandler.accept(treeNode);
                    }
                }
            }
        });

        // Add tree expansion/collapse listeners
        tree.addTreeExpansionListener(new javax.swing.event.TreeExpansionListener() {
            @Override
            public void treeExpanded(javax.swing.event.TreeExpansionEvent event) {
                if (expandHandler != null) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) event.getPath().getLastPathComponent();
                    TreeNode treeNode = (TreeNode) node.getUserObject();
                    expandHandler.accept(treeNode);
                }
            }

            @Override
            public void treeCollapsed(javax.swing.event.TreeExpansionEvent event) {
                if (collapseHandler != null) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) event.getPath().getLastPathComponent();
                    TreeNode treeNode = (TreeNode) node.getUserObject();
                    collapseHandler.accept(treeNode);
                }
            }
        });
    }

    public Tree onSelection(Consumer<TreeNode> handler) {
        this.selectionHandler = handler;
        return this;
    }

    public Tree onDoubleClick(Consumer<TreeNode> handler) {
        this.doubleClickHandler = handler;
        return this;
    }

    public Tree onExpand(Consumer<TreeNode> handler) {
        this.expandHandler = handler;
        return this;
    }

    public Tree onCollapse(Consumer<TreeNode> handler) {
        this.collapseHandler = handler;
        return this;
    }

    public Tree addNode(String parentPath, TreeNode newNode) {
        DefaultMutableTreeNode parentNode = findNodeByPath(parentPath);
        if (parentNode != null) {
            DefaultMutableTreeNode newSwingNode = createSwingNode(newNode);
            parentNode.add(newSwingNode);
            treeModel.nodeStructureChanged(parentNode);
        }
        return this;
    }

    public Tree addNode(TreeNode parentNode, TreeNode newNode) {
        DefaultMutableTreeNode parentSwingNode = findNodeByTreeNode(parentNode);
        if (parentSwingNode != null) {
            DefaultMutableTreeNode newSwingNode = createSwingNode(newNode);
            parentSwingNode.add(newSwingNode);
            treeModel.nodeStructureChanged(parentSwingNode);
        }
        return this;
    }

    public Tree removeNode(String nodePath) {
        DefaultMutableTreeNode nodeToRemove = findNodeByPath(nodePath);
        if (nodeToRemove != null && nodeToRemove != rootNode) {
            DefaultMutableTreeNode parent = (DefaultMutableTreeNode) nodeToRemove.getParent();
            treeModel.removeNodeFromParent(nodeToRemove);
        }
        return this;
    }

    public Tree removeNode(TreeNode node) {
        DefaultMutableTreeNode nodeToRemove = findNodeByTreeNode(node);
        if (nodeToRemove != null && nodeToRemove != rootNode) {
            treeModel.removeNodeFromParent(nodeToRemove);
        }
        return this;
    }

    public Tree expandAll() {
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
        return this;
    }

    public Tree collapseAll() {
        for (int i = tree.getRowCount() - 1; i >= 0; i--) {
            tree.collapseRow(i);
        }
        return this;
    }

    public Tree expandPath(String path) {
        DefaultMutableTreeNode node = findNodeByPath(path);
        if (node != null) {
            TreePath treePath = new TreePath(node.getPath());
            tree.expandPath(treePath);
        }
        return this;
    }

    public Tree collapsePath(String path) {
        DefaultMutableTreeNode node = findNodeByPath(path);
        if (node != null) {
            TreePath treePath = new TreePath(node.getPath());
            tree.collapsePath(treePath);
        }
        return this;
    }

    public Tree selectNode(String path) {
        DefaultMutableTreeNode node = findNodeByPath(path);
        if (node != null) {
            TreePath treePath = new TreePath(node.getPath());
            tree.setSelectionPath(treePath);
            tree.scrollPathToVisible(treePath);
        }
        return this;
    }

    public Tree selectNode(TreeNode node) {
        DefaultMutableTreeNode swingNode = findNodeByTreeNode(node);
        if (swingNode != null) {
            TreePath treePath = new TreePath(swingNode.getPath());
            tree.setSelectionPath(treePath);
            tree.scrollPathToVisible(treePath);
        }
        return this;
    }

    public Tree showRootHandles(boolean show) {
        tree.setShowsRootHandles(show);
        return this;
    }

    public Tree rootVisible(boolean visible) {
        tree.setRootVisible(visible);
        return this;
    }

    public Tree editable(boolean editable) {
        tree.setEditable(editable);
        return this;
    }

    public Tree rowHeight(int height) {
        tree.setRowHeight(height);
        return this;
    }

    public Tree autoRowHeight() {
        tree.setRowHeight(-1);
        return this;
    }

    public Tree visibleRowCount(int count) {
        tree.setVisibleRowCount(count);
        return this;
    }

    // Scroll pane configuration
    public Tree horizontalScrollPolicy(int policy) {
        scrollPane.setHorizontalScrollBarPolicy(policy);
        return this;
    }

    public Tree verticalScrollPolicy(int policy) {
        scrollPane.setVerticalScrollBarPolicy(policy);
        return this;
    }

    public Tree autoScrollHorizontal() {
        return horizontalScrollPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }

    public Tree autoScrollVertical() {
        return verticalScrollPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    }

    // Getters
    public TreeNode getSelectedNode() {
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        return selectedNode != null ? (TreeNode) selectedNode.getUserObject() : null;
    }

    public List<TreeNode> getSelectedNodes() {
        TreePath[] paths = tree.getSelectionPaths();
        List<TreeNode> selected = new ArrayList<>();
        
        if (paths != null) {
            for (TreePath path : paths) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                selected.add((TreeNode) node.getUserObject());
            }
        }
        
        return selected;
    }

    public TreeNode getRootNode() {
        return (TreeNode) rootNode.getUserObject();
    }

    public int getRowCount() {
        return tree.getRowCount();
    }

    // Helper methods
    private DefaultMutableTreeNode findNodeByPath(String path) {
        String[] parts = path.split("/");
        DefaultMutableTreeNode current = rootNode;
        
        // Skip root if path starts with root name
        int startIndex = 0;
        if (parts.length > 0 && parts[0].equals(((TreeNode) rootNode.getUserObject()).getTitle())) {
            startIndex = 1;
        }
        
        for (int i = startIndex; i < parts.length; i++) {
            String part = parts[i];
            DefaultMutableTreeNode found = null;
            
            for (int j = 0; j < current.getChildCount(); j++) {
                DefaultMutableTreeNode child = (DefaultMutableTreeNode) current.getChildAt(j);
                TreeNode treeNode = (TreeNode) child.getUserObject();
                if (treeNode.getTitle().equals(part)) {
                    found = child;
                    break;
                }
            }
            
            if (found == null) {
                return null;
            }
            current = found;
        }
        
        return current;
    }

    private DefaultMutableTreeNode findNodeByTreeNode(TreeNode target) {
        return findNodeRecursive(rootNode, target);
    }

    private DefaultMutableTreeNode findNodeRecursive(DefaultMutableTreeNode current, TreeNode target) {
        TreeNode currentTreeNode = (TreeNode) current.getUserObject();
        if (currentTreeNode.equals(target)) {
            return current;
        }
        
        for (int i = 0; i < current.getChildCount(); i++) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) current.getChildAt(i);
            DefaultMutableTreeNode result = findNodeRecursive(child, target);
            if (result != null) {
                return result;
            }
        }
        
        return null;
    }

    @Override
    public Tree build() {
        return this;
    }

    // TreeNode data class
    public static class TreeNode {
        private String title;
        private Object data;
        private Icon icon;
        private List<TreeNode> children = new ArrayList<>();
        private TreeNode parent;

        public TreeNode(String title) {
            this(title, null);
        }

        public TreeNode(String title, Object data) {
            this.title = title;
            this.data = data;
        }

        public static TreeNode create(String title) {
            return new TreeNode(title);
        }

        public static TreeNode create(String title, Object data) {
            return new TreeNode(title, data);
        }

        public TreeNode addChild(TreeNode child) {
            child.parent = this;
            children.add(child);
            return this;
        }

        public TreeNode addChild(String title) {
            TreeNode child = new TreeNode(title);
            return addChild(child);
        }

        public TreeNode addChild(String title, Object data) {
            TreeNode child = new TreeNode(title, data);
            return addChild(child);
        }

        public TreeNode removeChild(TreeNode child) {
            if (children.remove(child)) {
                child.parent = null;
            }
            return this;
        }

        public TreeNode removeChild(String title) {
            TreeNode child = findChild(title);
            if (child != null) {
                removeChild(child);
            }
            return this;
        }

        public TreeNode findChild(String title) {
            return children.stream()
                    .filter(child -> child.getTitle().equals(title))
                    .findFirst()
                    .orElse(null);
        }

        public boolean hasChildren() {
            return !children.isEmpty();
        }

        public boolean isLeaf() {
            return children.isEmpty();
        }

        public int getDepth() {
            int depth = 0;
            TreeNode current = parent;
            while (current != null) {
                depth++;
                current = current.parent;
            }
            return depth;
        }

        public String getPath() {
            List<String> pathParts = new ArrayList<>();
            TreeNode current = this;
            
            while (current != null) {
                pathParts.add(0, current.getTitle());
                current = current.parent;
            }
            
            return String.join("/", pathParts);
        }

        // Getters and setters
        public String getTitle() { return title; }
        public TreeNode setTitle(String title) { this.title = title; return this; }

        public Object getData() { return data; }
        public TreeNode setData(Object data) { this.data = data; return this; }

        public Icon getIcon() { return icon; }
        public TreeNode setIcon(Icon icon) { this.icon = icon; return this; }

        public List<TreeNode> getChildren() { return new ArrayList<>(children); }
        public TreeNode getParent() { return parent; }

        @Override
        public String toString() {
            return title;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            TreeNode treeNode = (TreeNode) obj;
            return title.equals(treeNode.title) && 
                   (data != null ? data.equals(treeNode.data) : treeNode.data == null);
        }

        @Override
        public int hashCode() {
            int result = title.hashCode();
            result = 31 * result + (data != null ? data.hashCode() : 0);
            return result;
        }
    }
}