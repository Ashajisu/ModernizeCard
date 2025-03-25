declare module 'wavesurfer.js/dist/plugins/regions.esm.js' {
    import { PluginDefinition } from 'wavesurfer.js';
    import type { RegionParams, Region } from 'wavesurfer.js/dist/plugins/regions.d.ts';

    export interface RegionsPluginOptions {
        regions?: RegionParams[];
        dragSelection?: boolean;
    }

    export interface RegionsPlugin {
        addRegion(options: RegionParams): Region;
        clearRegions(): void;
        getRegions(): Region[];
    }

    const RegionsPlugin: PluginDefinition<RegionsPluginOptions, RegionsPlugin>;
    export default RegionsPlugin;
}
