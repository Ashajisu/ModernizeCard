import WaveSurfer from 'wavesurfer.js'
import type { WaveSurferOptions } from 'wavesurfer.js';
import RegionsPlugin from 'wavesurfer.js/dist/plugins/regions.esm.js';
import type {RegionParams} from "wavesurfer.js/dist/plugins/regions";
import type {GenericPlugin} from "wavesurfer.js/dist/base-plugin";

// 기본 설정값
export const options:WaveSurferOptions & Record<string, any> = {
    /** HTML element or CSS selector (required) */
    container: '#waveform',
    /** The height of the waveform in pixels */
    height: 128,
    /** The width of the waveform in pixels or any CSS value; defaults to 100% */
    width: '100%',
    /** Render each audio channel as a separate waveform */
    // splitChannels: false,
    /** Stretch the waveform to the full height */
    normalize: true,
    /** The color of the waveform */
    waveColor: 'violet',
    /** The color of the progress mask */
    progressColor: 'purple',
    /** The color of the play pack cursor */
    cursorColor: '#ddd5e9',
    /** The cursor width */
    cursorWidth: 1,
    /** Render the waveform with bars like this: ▁ ▂ ▇ ▃ ▅ ▂ */
    barWidth: NaN,
    /** Spacing between bars in pixels */
    barGap: NaN,
    /** Rounded borders for bars */
    barRadius: NaN,
    /** A vertical scaling factor for the waveform */
    barHeight: NaN,
    /** Vertical bar alignment **/
    //barAlign: '',
    /** Minimum pixels per second of audio (i.e. zoom level) */
    minPxPerSec: 1,
    /** Stretch the waveform to fill the container, true by default */
    fillParent: true,
    /** Audio URL */
    // url: '/public/assets/audio/PartOfYourWorld.wav',
    url: '/public/assets/audio/test.wav',
    /** Whether to show default audio element controls */
    mediaControls: true,
    /** Play the audio on load */
    autoplay: false,
    /** Pass false to disable clicks on the waveform */
    interact: true,
    /** Allow to drag the cursor to seek to a new position */
    dragToSeek: false,
    /** Hide the scrollbar */
    hideScrollbar: false,
    /** Audio rate */
    audioRate: 1,
    /** Automatically scroll the container to keep the current position in viewport */
    autoScroll: true,
    /** If autoScroll is enabled, keep the cursor in the center of the waveform during playback */
    autoCenter: true,
    /** Decoding sample rate. Doesn't affect the playback. Defaults to 8000 */
    sampleRate: 8000,
}

// WaveSurfer 인스턴스를 생성하고 이벤트를 설정하는 함수
/** Guide to
 * import { onMounted } from 'vue';
 * import { createWaveSurferWithOptions } from '@/plugins/Wavesufer';
 *
 * onMounted(() => {
 *   // 음성파일로 파형 및 오디오 생성
 *   // createWaveSurferWithRegions("/public/assets/audio/PartOfYourWorld.wav");
 * });
 * **/
export const createWaveSurferWithRegions = (audioUrl: string) => {
    const regions = RegionsPlugin.create(); // 중복사용 금지. 항상 새로 생성해야함.

    // WaveSurfer 인스턴스 생성
    const wavesurfer = WaveSurfer.create({
        ...options,
        url: audioUrl,  // 동적으로 URL 설정
        plugins: [regions], // RegionsPlugin 활성화
    });

    setupWaveSurferEvents(wavesurfer, regions);
    return wavesurfer;
};

// 옵션 사용자 지정 생성자
/**
 * Guide to
 * import { onMounted } from 'vue';
 * import { createWaveSurferWithOptions, options } from '@/plugins/Wavesufer';
 *
 * onMounted(() => {
 *   options.barAlign = 'top';
 *   options.url = '/public/assets/audio/PartOfYourWorld.wav';
 *   createWaveSurferWithOptions(options);
 * });
 * **/
export const createWaveSurferWithOptions = (options:WaveSurferOptions & Record<string, any>) => {
    const regions: GenericPlugin = RegionsPlugin.create(); // 중복사용 금지. 항상 새로 생성해야함.
    options.plugins = [regions];

    // WaveSurfer 인스턴스 생성
    const wavesurfer = WaveSurfer.create(options);
    setupWaveSurferEvents(wavesurfer, regions);

    return wavesurfer;
};

// 이벤트 설정 함수
const loop = true;
export const setupWaveSurferEvents = (wavesurfer: WaveSurfer, regions: any) => {
    let activeRegion: RegionParams[]| null = null;
    wavesurfer.on('ready', () => {
        wavesurfer.setTime(10);
    });

    // 인터랙션 시 activeRegion 초기화
    wavesurfer.on('interaction', () => {
        activeRegion = null;
    });

    // 디코드가 끝나면 region 을 추가하고 이벤트 설정
    wavesurfer.on('decode', () => {
        console.log('decode_ setting regions');

        // region 추가
        regions.addRegion({
            start: 5,  // 5초 지점부터 시작
            end: 10,   // 10초 지점까지
            color: 'rgba(0, 0, 255, 0.2)',
        });

        // region 클릭 시 재생
        regions.on('region-clicked', (region:any, e:any) => {
            e.stopPropagation();  // 파형의 클릭 이벤트를 방지
            activeRegion = region;
            region.play(true);  // region 을 강제로 재생
        });

        // region 영역을 벗어나면 루프 실행
        regions.on('region-out', (region:any) => {
            if (activeRegion === region) {
                if (loop) {
                    region.play();
                } else {
                    activeRegion = null;
                }
            }
        });
    });

}