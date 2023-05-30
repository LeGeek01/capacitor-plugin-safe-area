import { WebPlugin } from '@capacitor/core';

import type { SafeAreaPlugin, SafeAreaInsets, StatusBarInfo, NavigationBarInfo } from './definitions';


export class SafeAreaWeb extends WebPlugin implements SafeAreaPlugin {
  async getSafeAreaInsets(): Promise<SafeAreaInsets> {
    return {
      insets: {
        top: 0,
        left: 0,
        right: 0,
        bottom: 0,
      }
    };
  }
  async getStatusBarHeight(): Promise<StatusBarInfo> {
    // throw this.unimplemented('Method not supported on Web.');
    return {
      statusBarHeight: 0
    }
  }
  async getNavigationBarHeight(): Promise<NavigationBarInfo> {
    // throw this.unimplemented('Method not supported on Web.');
    return {
      navigationBarHeight: 0
    }
  }
}
