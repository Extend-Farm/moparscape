package io.github.ffakira.moparscape.client;

import io.github.ffakira.moparscape.net.PacketBuffer;

final class RoutePlanner {

    private RoutePlanner() {
    }

    static boolean route(int i, int j, int k, int l, int i1, int j1, int k1, int l1, int i2, int j2, boolean flag, int k2,
                         int anInt918, CollisionMap[] collisionMaps, int[][] pathDirection, int[][] pathCost, int[] pathXQueue, int[] pathYQueue,
                         int anInt1034, int anInt1035, PacketBuffer outboundBuffer, int[] inputState, int[] routeState)
    {
        byte byte0 = 104;
        byte byte1 = 104;
        for(int l2 = 0; l2 < byte0; l2++)
        {
            for(int i3 = 0; i3 < byte1; i3++)
            {
                pathDirection[l2][i3] = 0;
                pathCost[l2][i3] = 0x5f5e0ff;
            }

        }

        int j3 = j2;
        int k3 = j1;
        pathDirection[j2][j1] = 99;
        pathCost[j2][j1] = 0;
        int l3 = 0;
        int i4 = 0;
        pathXQueue[l3] = j2;
        pathYQueue[l3++] = j1;
        boolean flag1 = false;
        int j4 = pathXQueue.length;
        int ai[][] = collisionMaps[anInt918].anIntArrayArray294;
        while(i4 != l3)
        {
            j3 = pathXQueue[i4];
            k3 = pathYQueue[i4];
            i4 = (i4 + 1) % j4;
            if(j3 == k2 && k3 == i2)
            {
                flag1 = true;
                break;
            }
            if(i1 != 0)
            {
                if((i1 < 5 || i1 == 10) && collisionMaps[anInt918].method219(k2, j3, k3, 0, j, i1 - 1, i2))
                {
                    flag1 = true;
                    break;
                }
                if(i1 < 10 && collisionMaps[anInt918].method220(k2, i2, k3, i1 - 1, j, j3, 0))
                {
                    flag1 = true;
                    break;
                }
            }
            if(k1 != 0 && k != 0 && collisionMaps[anInt918].method221((byte)1, i2, k2, j3, k, l1, k1, k3))
            {
                flag1 = true;
                break;
            }
            int l4 = pathCost[j3][k3] + 1;
            if(j3 > 0 && pathDirection[j3 - 1][k3] == 0 && (ai[j3 - 1][k3] & 0x1280108) == 0)
            {
                pathXQueue[l3] = j3 - 1;
                pathYQueue[l3] = k3;
                l3 = (l3 + 1) % j4;
                pathDirection[j3 - 1][k3] = 2;
                pathCost[j3 - 1][k3] = l4;
            }
            if(j3 < byte0 - 1 && pathDirection[j3 + 1][k3] == 0 && (ai[j3 + 1][k3] & 0x1280180) == 0)
            {
                pathXQueue[l3] = j3 + 1;
                pathYQueue[l3] = k3;
                l3 = (l3 + 1) % j4;
                pathDirection[j3 + 1][k3] = 8;
                pathCost[j3 + 1][k3] = l4;
            }
            if(k3 > 0 && pathDirection[j3][k3 - 1] == 0 && (ai[j3][k3 - 1] & 0x1280102) == 0)
            {
                pathXQueue[l3] = j3;
                pathYQueue[l3] = k3 - 1;
                l3 = (l3 + 1) % j4;
                pathDirection[j3][k3 - 1] = 1;
                pathCost[j3][k3 - 1] = l4;
            }
            if(k3 < byte1 - 1 && pathDirection[j3][k3 + 1] == 0 && (ai[j3][k3 + 1] & 0x1280120) == 0)
            {
                pathXQueue[l3] = j3;
                pathYQueue[l3] = k3 + 1;
                l3 = (l3 + 1) % j4;
                pathDirection[j3][k3 + 1] = 4;
                pathCost[j3][k3 + 1] = l4;
            }
            if(j3 > 0 && k3 > 0 && pathDirection[j3 - 1][k3 - 1] == 0 && (ai[j3 - 1][k3 - 1] & 0x128010e) == 0 && (ai[j3 - 1][k3] & 0x1280108) == 0 && (ai[j3][k3 - 1] & 0x1280102) == 0)
            {
                pathXQueue[l3] = j3 - 1;
                pathYQueue[l3] = k3 - 1;
                l3 = (l3 + 1) % j4;
                pathDirection[j3 - 1][k3 - 1] = 3;
                pathCost[j3 - 1][k3 - 1] = l4;
            }
            if(j3 < byte0 - 1 && k3 > 0 && pathDirection[j3 + 1][k3 - 1] == 0 && (ai[j3 + 1][k3 - 1] & 0x1280183) == 0 && (ai[j3 + 1][k3] & 0x1280180) == 0 && (ai[j3][k3 - 1] & 0x1280102) == 0)
            {
                pathXQueue[l3] = j3 + 1;
                pathYQueue[l3] = k3 - 1;
                l3 = (l3 + 1) % j4;
                pathDirection[j3 + 1][k3 - 1] = 9;
                pathCost[j3 + 1][k3 - 1] = l4;
            }
            if(j3 > 0 && k3 < byte1 - 1 && pathDirection[j3 - 1][k3 + 1] == 0 && (ai[j3 - 1][k3 + 1] & 0x1280138) == 0 && (ai[j3 - 1][k3] & 0x1280108) == 0 && (ai[j3][k3 + 1] & 0x1280120) == 0)
            {
                pathXQueue[l3] = j3 - 1;
                pathYQueue[l3] = k3 + 1;
                l3 = (l3 + 1) % j4;
                pathDirection[j3 - 1][k3 + 1] = 6;
                pathCost[j3 - 1][k3 + 1] = l4;
            }
            if(j3 < byte0 - 1 && k3 < byte1 - 1 && pathDirection[j3 + 1][k3 + 1] == 0 && (ai[j3 + 1][k3 + 1] & 0x12801e0) == 0 && (ai[j3 + 1][k3] & 0x1280180) == 0 && (ai[j3][k3 + 1] & 0x1280120) == 0)
            {
                pathXQueue[l3] = j3 + 1;
                pathYQueue[l3] = k3 + 1;
                l3 = (l3 + 1) % j4;
                pathDirection[j3 + 1][k3 + 1] = 12;
                pathCost[j3 + 1][k3 + 1] = l4;
            }
        }
        routeState[0] = 0;
        if(!flag1)
        {
            if(flag)
            {
                int i5 = 100;
                for(int k5 = 1; k5 < 2; k5++)
                {
                    for(int i6 = k2 - k5; i6 <= k2 + k5; i6++)
                    {
                        for(int l6 = i2 - k5; l6 <= i2 + k5; l6++)
                            if(i6 >= 0 && l6 >= 0 && i6 < 104 && l6 < 104 && pathCost[i6][l6] < i5)
                            {
                                i5 = pathCost[i6][l6];
                                j3 = i6;
                                k3 = l6;
                                routeState[0] = 1;
                                flag1 = true;
                            }

                    }

                    if(flag1)
                        break;
                }

            }
            if(!flag1)
                return false;
        }
        i4 = 0;
        pathXQueue[i4] = j3;
        pathYQueue[i4++] = k3;
        if(l != -11308)
        {
            for(int j6 = 1; j6 > 0; j6++);
        }
        int l5;
        for(int j5 = l5 = pathDirection[j3][k3]; j3 != j2 || k3 != j1; j5 = pathDirection[j3][k3])
        {
            if(j5 != l5)
            {
                l5 = j5;
                pathXQueue[i4] = j3;
                pathYQueue[i4++] = k3;
            }
            if((j5 & 2) != 0)
                j3++;
            else
            if((j5 & 8) != 0)
                j3--;
            if((j5 & 1) != 0)
                k3++;
            else
            if((j5 & 4) != 0)
                k3--;
        }

        if(i4 > 0)
        {
            int k4 = i4;
            if(k4 > 25)
                k4 = 25;
            i4--;
            int k6 = pathXQueue[i4];
            int i7 = pathYQueue[i4];
            routeState[1] += k4;
            if(routeState[1] >= 92)
            {
                outboundBuffer.method397((byte)6, 36);
                outboundBuffer.method402(0);
                routeState[1] = 0;
            }
            if(i == 0)
            {
                outboundBuffer.method397((byte)6, 164);
                outboundBuffer.method398(k4 + k4 + 3);
            }
            if(i == 1)
            {
                outboundBuffer.method397((byte)6, 248);
                outboundBuffer.method398(k4 + k4 + 3 + 14);
            }
            if(i == 2)
            {
                outboundBuffer.method397((byte)6, 98);
                outboundBuffer.method398(k4 + k4 + 3);
            }
            outboundBuffer.method433(0, k6 + anInt1034);
            routeState[2] = pathXQueue[0];
            routeState[3] = pathYQueue[0];
            for(int j7 = 1; j7 < k4; j7++)
            {
                i4--;
                outboundBuffer.method398(pathXQueue[i4] - k6);
                outboundBuffer.method398(pathYQueue[i4] - i7);
            }

            outboundBuffer.method431(true, i7 + anInt1035);
            outboundBuffer.method424(inputState[5] != 1 ? 0 : 1, 0);
            return true;
        }
        return i != 1;
    }
}
