import { defineStore } from 'pinia';

export const useTagsViewStore = defineStore('tagsView', {
    state: () => ({
        visitedViews: [],
        cachedViews: []
    }),
    actions: {
        addView(view) {
            if (this.visitedViews.some(v => v.path === view.path)) return;
            this.visitedViews.push(
                Object.assign({}, view, {
                    title: view.meta.title || 'no-name'
                })
            );
            if (view.name && !view.meta.noCache) {
                this.cachedViews.push(view.name);
            }
        },
        delView(view) {
            return new Promise(resolve => {
                this.visitedViews = this.visitedViews.filter(v => v.path !== view.path);
                this.cachedViews = this.cachedViews.filter(v => v !== view.name);
                resolve([...this.visitedViews]);
            });
        },
        delOthersViews(view) {
            this.visitedViews = this.visitedViews.filter(v => {
                return v.meta.affix || v.path === view.path;
            });
            this.cachedViews = this.cachedViews.filter(v => {
                return v.meta.affix || v.name === view.name;
            });
        },
        delAllViews() {
            this.visitedViews = this.visitedViews.filter(v => v.meta.affix);
            this.cachedViews = this.cachedViews.filter(v => v.meta.affix)
        },
    }
});